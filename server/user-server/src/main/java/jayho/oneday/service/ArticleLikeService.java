package jayho.oneday.service;

import jayho.oneday.ArticleLikeCountEvent;
import jayho.oneday.adaptor.producer.ArticleLikeProducer;
import jayho.oneday.entity.ArticleLike;
import jayho.oneday.entity.ArticleLikeCount;
import jayho.oneday.entity.id.LikeId;
import jayho.oneday.repository.ArticleLikeCountRepository;
import jayho.oneday.repository.ArticleLikeMemoryRepository;
import jayho.oneday.repository.ArticleLikeRepository;
import jayho.oneday.service.response.ArticleLikeCountResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository; // rdb
    private final ArticleLikeCountRepository articleLikeCountRepository; // rdb
    private final ArticleLikeCountJdbcRepository articleLikeCountJdbcRepository; // rdb
    private final ArticleLikeMemoryRepository articleLikeMemoryRepository; // memory
    private final ArticleLikeReadService articleLikeReadService; // memory
    private final ArticleLikeProducer articleLikeProducer; // mq


    // article like message >> MQ
    public void articleLikeToMQ(Long articleId, Long userId) {
        articleLikeProducer.articleLikeMessage(articleId, userId, true);
    }

    public void validateArticleLikeBulk(List<ArticleLike> articleLikeList) {
        validateArticleLikeList(articleLikeList);
    }

    // bulk
    // MQ(article like) >> message 정합성 check >> MQ(article like count)
    private void validateArticleLikeList(List<ArticleLike> articleLikeList) {
        List<ArticleLike> sucessInsertList = new ArrayList<>();
        List<ArticleLike> failInsertList = new ArrayList<>();

        // bulk insert / fail insert : update retry
        int[] resultArr = articleLikeRepository.saveAll(articleLikeList);
        IntStream.range(0, articleLikeList.size()).forEach(idx -> {
            if (resultArr[idx] == 1) {
                sucessInsertList.add(articleLikeList.get(idx));
            } else {
                failInsertList.add(articleLikeList.get(idx));
            }
        });

        // successInsertList
        articleLikeProducer.articleLikeCountMessage(
                getArticleLikeCountEventMap(sucessInsertList)
        );
        // failInsertList :
        failInsertRetryToUpdate(failInsertList);
    }

    @NotNull
    private Map<Long, ArticleLikeCountEvent> getArticleLikeCountEventMap(List<ArticleLike> sucessInsertList) {
        Map<Long, ArticleLikeCountEvent> map = new HashMap<>();
        sucessInsertList.forEach(articleLike -> {
            map.computeIfAbsent(articleLike.getArticleId(), k ->
                    ArticleLikeCountEvent.newBuilder()
                            .setArticleId(articleLike.getArticleId())
                            .setCount(map.get(articleLike.getArticleId()).getCount() + 1)
                            .setLike(articleLike.getLiked())
                            .build());
        });
        return map;
    }

    private void failInsertRetryToUpdate(List<ArticleLike> failInsertList) {
        // TODO 한번에 select해서 넣는 로직으로 변경할 것 >> 많지 않을 거 같다면 단건
        failInsertList.forEach(articleLike -> {
            articleLikeRepository.findById(LikeId.create(articleLike.getArticleId(), articleLike.getUserId()))
                    .ifPresent((existArticleLike -> {
                        if (existArticleLike.getLiked() == true) {
                            log.info("중복 된 key 입니다. articleId:{}, userId:{} ",existArticleLike.getArticleId(), existArticleLike.getUserId());
                            return;
                        }
                        existArticleLike.setLiked(true);
                        ArticleLike updatedArticle = articleLikeRepository.save(existArticleLike);
                        articleLikeProducer.articleLikeCountMessage(updatedArticle.getArticleId(), 1L, updatedArticle.getLiked());
                    })
            );
        });
    }

    // MQ(article like count) >> message counting >> DB (counting)
    public void articleLikeCountingMQ(Long articleId, Boolean liked, Long likeBundleCount) {
        articleLikeCountRepository.findById(articleId)
                .ifPresentOrElse(
                        articleLikeCount -> {
                            Long updateArticleCount = liked == true ?
                                    articleLikeCount.getLikeCount() + likeBundleCount :
                                    articleLikeCount.getLikeCount() - likeBundleCount;
                            articleLikeCount.setLikeCount(updateArticleCount);
                            articleLikeCountRepository.save(articleLikeCount);
                            articleLikeMemoryRepository.setLikeCount(articleId, updateArticleCount);
                        }, () -> {
                            articleLikeCountRepository.save(ArticleLikeCount.create(articleId, likeBundleCount));
                            articleLikeMemoryRepository.increase(articleId);
                        }
                );
    }

    // MQ(article like count) >> message counting >> DB (counting)
    public void articleLikeCountingMQ2(List<ArticleLikeCount> articleLikeCountList) {
        // update >> res 0 >> bulk insert
        // select 해야나네...

        // 얘네만 update
        List<ArticleLikeCount> forUpdateArticleCountList = articleLikeCountRepository.findByArticleIdIn(
                articleLikeCountList.stream()
                        .map(ArticleLikeCount::getArticleId).toList()
        );
        Map<Long, ArticleLikeCount> forUpdateArticleCountMap = forUpdateArticleCountList.stream()
                .collect(Collectors.toMap(
                        ArticleLikeCount::getArticleId,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));

        List<ArticleLikeCount> forInsertArticleCountList = new ArrayList<>();
        articleLikeCountList.forEach(articleLikeCount -> {
            if (!(forUpdateArticleCountMap.containsKey(articleLikeCount.getArticleId()))) {
                forInsertArticleCountList.add(articleLikeCount);
            }
        });

        int[] resultArr = articleLikeCountJdbcRepository.updateAll(forUpdateArticleCountList);
        IntStream.range(0, forUpdateArticleCountList.size()).forEach(idx -> {
            Long articleId = forUpdateArticleCountList.get(idx).getArticleId();
            articleLikeMemoryRepository.setLikeCount(
                    articleId,
                    forUpdateArticleCountMap.get(articleId).getLikeCount() +
                            articleLikeCountList.get(idx).getLikeCount()
                    );
        });

        // 나머지
        articleLikeCountJdbcRepository.saveAll(forInsertArticleCountList);
    }


    public void articleUnlikeMQ(Long articleId, Long userId) {
        articleLikeRepository.findById(LikeId.create(articleId, userId))
                .ifPresentOrElse(articleLike -> {
                    articleLike.setLiked(false);
                    articleLike.setModifiedAt(LocalDateTime.now());
                    articleLikeProducer.articleLikeMessage(articleId, userId, articleLike.getLiked());
                }, () -> {
                    articleLikeRepository.save(
                            ArticleLike.create(articleId, userId, false)
                    );
                    articleLikeProducer.articleLikeMessage(articleId, userId, false);
                });
    }

    public ArticleLikeCountResponseData readLikeCount(Long articleId) {
        Long articleLIkeCount;
        if ((articleLIkeCount=articleLikeReadService.readLikeCount(articleId))<0) {
            articleLIkeCount = 0L;
        }
        return ArticleLikeCountResponseData.from(
                ArticleLikeCount.create(articleId, articleLIkeCount));
    }

}

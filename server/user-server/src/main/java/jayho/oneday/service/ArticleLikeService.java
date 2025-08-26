package jayho.oneday.service;

import jayho.oneday.adaptor.producer.ArticleLikeProducer;
import jayho.oneday.controller.exception.NegativeException;
import jayho.oneday.entity.ArticleLike;
import jayho.oneday.entity.ArticleLikeCount;
import jayho.oneday.entity.id.LikeId;
import jayho.oneday.repository.ArticleLikeCountRepository;
import jayho.oneday.repository.ArticleLikeMemoryRepository;
import jayho.oneday.repository.ArticleLikeRepository;
import jayho.oneday.service.response.ArticleLikeCountResponseData;
import jayho.oneday.service.response.ArticleLikeResponseData;
import jayho.oneday.service.response.ArticleViewResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository; // rdb
    private final ArticleLikeCountRepository articleLikeCountRepository; // rdb
    private final ArticleLikeMemoryRepository articleLikeMemoryRepository; // memory
    private final ArticleLikeReadService articleLikeReadService; // memory
    private final ArticleLikeProducer articleLikeProducer; // mq

    @Transactional
    public ArticleLikeResponseData articleLikeMQ(Long articleId, Long userId){
        ArticleLike articleLike = ArticleLike.create(articleId, userId);
        try {
            articleLikeRepository.saveArticleLike(articleLike.getArticleId(),
                    articleLike.getUserId(),
                    articleLike.getLiked(),
                    articleLike.getCreatedAt(),
                    articleLike.getModifiedAt());
            articleLikeProducer.articleLikeMessage(articleId, userId, articleLike.getLiked());
        } catch (DataIntegrityViolationException e) {
            articleLike = articleLikeRepository.findById(LikeId.create(articleId, userId))
                    .map(updateArticleLike -> {
                        updateArticleLike.setLiked(true);
                        updateArticleLike.setModifiedAt(LocalDateTime.now());
                        articleLikeProducer.articleLikeMessage(articleId, userId, updateArticleLike.getLiked());
                        return updateArticleLike;
                    }).orElseThrow();
        }
        return ArticleLikeResponseData.from(articleLike);
    }

    public ArticleLikeResponseData articleUnlikeMQ(Long articleId, Long userId) {

        ArticleLike articleLike = ArticleLike.create(articleId, userId);
        articleLike.setLiked(false);
        articleLike.setModifiedAt(LocalDateTime.now());
        ArticleLike articleUnlike = articleLikeRepository.save(articleLike);

        articleLikeCountRepository.findById(articleId)
                .ifPresent(articleLikeCount ->{
                    articleLikeCount.setLikeCount(articleLikeCount.getLikeCount()-1);
                    articleLikeCountRepository.save(articleLikeCount);
                });

        articleLikeProducer.articleLikeMessage(articleId, userId, articleLike.getLiked());
        return ArticleLikeResponseData.from(articleUnlike);
    }

    public void articleLikeCountingMQ(Long articleId, Boolean liked) throws NegativeException {
        articleLikeCountRepository.findById(articleId)
                .ifPresentOrElse(
                        articleLikeCount -> {
                            articleLikeCount.setLikeCount(
                                    liked == true ?
                                            articleLikeCount.getLikeCount() + 1 :
                                            articleLikeCount.getLikeCount() - 1
                            );
                            articleLikeCountRepository.save(articleLikeCount);
                            articleLikeMemoryRepository.setLikeCount(articleId, articleLikeCount.getLikeCount());
                        },
                        () -> articleLikeCountRepository.save(
                                ArticleLikeCount.create(articleId)));
    }

//    private Long checkLikeCountInCache(Long articleId, Boolean liked) {
//        return liked == true ? articleLikeMemoryRepository.increase(articleId) :
//                               articleLikeMemoryRepository.decrease(articleId);
//    }

    public ArticleLikeCountResponseData readLikeCount(Long articleId) {
        Long articleLIkeCount;
        if ((articleLIkeCount=articleLikeReadService.readLikeCount(articleId))<0) {
            articleLIkeCount = 0L;
        }
        return ArticleLikeCountResponseData.from(
                ArticleLikeCount.create(articleId, articleLIkeCount));
    }


//    public ArticleLikeResponseData articleLike(Long articleId, Long userId) {
//
//        ArticleLike articleLike = saveArticleLike(articleId, userId);
//
//        // article like가 이미 존재한다면 update
//        if (articleLike==null){
//            articleLike = updateArticleLike(articleId, userId);
//        }
//
//        // article like counting
//        articleLikeCounting(articleId);
//
//        assert articleLike != null;
//        articleLikeProducer.articleLikeMessage(articleId, userId, articleLike.getLiked());
//        return ArticleLikeResponseData.from(articleLike);
//    }
//
//    @Nullable
//    private ArticleLike saveArticleLike(Long articleId, Long userId) {
//        return transactionTemplate.execute(status -> {
//            try {
//                ArticleLike insertArticleLike = ArticleLike.create(articleId, userId);
//                articleLikeRepository.saveArticleLike(insertArticleLike.getArticleId(),
//                        insertArticleLike.getUserId(),
//                        insertArticleLike.getLiked(),
//                        insertArticleLike.getCreatedAt(),
//                        insertArticleLike.getModifiedAt());
//
//                // article like counting
//                articleLikeCounting(articleId);
//                return insertArticleLike;
//            } catch (DataIntegrityViolationException e) {
//                status.setRollbackOnly();
//                return null;
//            }
//        });
//    }
//
//    private ArticleLike updateArticleLike(Long articleId, Long userId) {
//        return transactionTemplate.execute(status -> articleLikeRepository.findById(LikeId.create(articleId, userId))
//                .map(updateArticleLike -> {
//                    updateArticleLike.setLiked(true);
//                    updateArticleLike.setModifiedAt(LocalDateTime.now());
//
//                    // article like counting
//                    articleLikeCounting(articleId);
//                    return articleLikeRepository.save(updateArticleLike);
//                }).orElseThrow());
//    }
//
//    private void articleLikeCounting(Long articleId) {
//        articleLikeCountRepository.findById(articleId)
//                .ifPresentOrElse(
//                        articleLikeCount -> {
//                            articleLikeCount.setLikeCount(articleLikeCount.getLikeCount() + 1);
//                            articleLikeCountRepository.save(articleLikeCount);
//                        },
//                        () -> articleLikeCountRepository.save(
//                                ArticleLikeCount.create(articleId)));
//    }



}

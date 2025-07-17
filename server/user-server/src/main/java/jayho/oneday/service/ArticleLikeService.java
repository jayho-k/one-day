package jayho.oneday.service;

import jayho.oneday.entity.ArticleLike;
import jayho.oneday.entity.ArticleLikeCount;
import jayho.oneday.repository.ArticleLikeCountRepository;
import jayho.oneday.repository.ArticleLikeRepository;
import jayho.oneday.service.response.ArticleLikeCountResponseData;
import jayho.oneday.service.response.ArticleLikeResponseData;
import jayho.oneday.service.response.ArticleViewResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    @Transactional
    public ArticleLikeResponseData articleLike(Long articleId, Long userId) {
        ArticleLike articleLike = ArticleLike.create(articleId, userId);
        int result = articleLikeRepository.saveArticleLike(
                articleLike.getArticleId(),
                articleLike.getUserId(),
                articleLike.getLiked(),
                articleLike.getCreatedAt(),
                articleLike.getModifiedAt()
        );
        if (result<1){
            throw new IllegalStateException("There are no changes to any columns.");
        }

        // res > 0 이상 처리를 data module에서 해줘야할지 고민 필요
        articleLikeCountRepository.findById(articleId)
                        .ifPresentOrElse(
                                articleLikeCount ->{
                                    articleLikeCount.setLikeCount(articleLikeCount.getLikeCount()+1);
                                    articleLikeCountRepository.save(articleLikeCount);
                                },
                                ()-> articleLikeCountRepository.save(
                                        ArticleLikeCount.create(articleId))
                        );
        return ArticleLikeResponseData.from(articleLike);
    }

    @Transactional
    public ArticleLikeResponseData articleUnlike(Long articleId, Long userId) {
        ArticleLike articleLike = ArticleLike.create(articleId, userId);
        articleLike.setLiked(false);
        articleLike.setModifiedAt(LocalDateTime.now());
        ArticleLike articleUnlike = articleLikeRepository.save(articleLike);

        articleLikeCountRepository.findById(articleId)
                .ifPresent(articleLikeCount ->{
                    articleLikeCount.setLikeCount(articleLikeCount.getLikeCount()-1);
                    articleLikeCountRepository.save(articleLikeCount);
                });

        return ArticleLikeResponseData.from(articleLike);
    }

    public ArticleLikeCountResponseData readLikeCount(Long articleId) {
        return articleLikeCountRepository.findById(articleId)
                .map(ArticleLikeCountResponseData::from)
                .orElseThrow();
    }

}

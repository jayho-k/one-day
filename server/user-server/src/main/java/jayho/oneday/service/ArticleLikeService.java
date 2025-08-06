package jayho.oneday.service;

import jayho.oneday.entity.ArticleLike;
import jayho.oneday.entity.ArticleLikeCount;
import jayho.oneday.entity.id.LikeId;
import jayho.oneday.repository.ArticleLikeCountRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final TransactionTemplate transactionTemplate;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    public ArticleLikeResponseData articleLike(Long articleId, Long userId) {

        ArticleLike articleLike = saveArticleLike(articleId, userId);

        // article like가 이미 존재한다면 update
        if (articleLike==null){
            articleLike = updateArticleLike(articleId, userId);
        }

        // article like counting
        articleLikeCounting(articleId);

        assert articleLike != null;
        return ArticleLikeResponseData.from(articleLike);
    }

    @Nullable
    private ArticleLike saveArticleLike(Long articleId, Long userId) {
        return transactionTemplate.execute(status -> {
            try {
                ArticleLike insertArticleLike = ArticleLike.create(articleId, userId);
                articleLikeRepository.saveArticleLike(insertArticleLike.getArticleId(),
                        insertArticleLike.getUserId(),
                        insertArticleLike.getLiked(),
                        insertArticleLike.getCreatedAt(),
                        insertArticleLike.getModifiedAt());

                // article like counting
                articleLikeCounting(articleId);
                return insertArticleLike;
            } catch (DataIntegrityViolationException e) {
                status.setRollbackOnly();
                return null;
            }
        });
    }

    private ArticleLike updateArticleLike(Long articleId, Long userId) {
        return transactionTemplate.execute(status -> articleLikeRepository.findById(LikeId.create(articleId, userId))
                .map(updateArticleLike -> {
                    updateArticleLike.setLiked(true);
                    updateArticleLike.setModifiedAt(LocalDateTime.now());

                    // article like counting
                    articleLikeCounting(articleId);
                    return articleLikeRepository.save(updateArticleLike);
                }).orElseThrow());
    }

    private void articleLikeCounting(Long articleId) {
        articleLikeCountRepository.findById(articleId)
                .ifPresentOrElse(
                        articleLikeCount -> {
                            articleLikeCount.setLikeCount(articleLikeCount.getLikeCount() + 1);
                            articleLikeCountRepository.save(articleLikeCount);
                        },
                        () -> articleLikeCountRepository.save(
                                ArticleLikeCount.create(articleId)));
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

        return ArticleLikeResponseData.from(articleUnlike);
    }

    public ArticleLikeCountResponseData readLikeCount(Long articleId) {
        return articleLikeCountRepository.findById(articleId)
                .map(ArticleLikeCountResponseData::from)
                .orElseThrow();
    }

}

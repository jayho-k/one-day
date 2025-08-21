package jayho.oneday.repository;

import jayho.oneday.entity.ArticleLike;
import jayho.oneday.entity.id.LikeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleLikeRepository {

    private final ArticleLikeJpaRepository articleLikeJpaRepository;
    private final ArticleLikeJdbcRepository articleLikeJdbcRepository;

    public ArticleLike save(ArticleLike articleLike) {
        return articleLikeJpaRepository.save(articleLike);
    }

    public int saveArticleLike(Long articleId, Long userId, Boolean like, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        return articleLikeJpaRepository.saveArticleLike(articleId, userId, like, createdAt, modifiedAt);
    }

    public int[] saveAll(List<ArticleLike> articleLikeList) {
        return articleLikeJdbcRepository.saveAll(articleLikeList);
    }

    public Optional<ArticleLike> findById(LikeId likeId) {
        return articleLikeJpaRepository.findById(likeId);
    }

}

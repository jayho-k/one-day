package jayho.oneday.repository;

import jayho.oneday.entity.ArticleLike;
import jayho.oneday.entity.id.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, LikeId> {

    @Query(
            value = "insert into article_like (article_id, user_id, liked, created_at, modified_at) " +
                    "values (:articleId, :userId, :liked, :createdAt, :modifiedAt) " +
                    "on conflict (article_id, user_id) " +
                    "do update set " +
                    "liked = true, " +
                    "modified_at = :modifiedAt;"
            ,nativeQuery = true
    )
    @Modifying
    int saveArticleLike(@Param("articleId") Long articleId,
                        @Param("userId") Long userId,
                        @Param("liked") Boolean liked,
                        @Param("createdAt") LocalDateTime createdAt,
                        @Param("modifiedAt") LocalDateTime modifiedAt
    );

}

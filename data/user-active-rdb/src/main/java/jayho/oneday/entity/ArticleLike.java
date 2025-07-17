package jayho.oneday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jayho.oneday.entity.id.LikeId;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@IdClass(LikeId.class)
public class ArticleLike {

    @Id
    private Long articleId;
    @Id
    private Long userId;
    @Setter
    private Boolean liked;
    private LocalDateTime createdAt;
    @Setter
    private LocalDateTime modifiedAt;

    public static ArticleLike create(Long articleId, Long userId) {
        ArticleLike like = new ArticleLike();
        like.articleId = articleId;
        like.userId = userId;
        like.liked = true;
        like.createdAt = LocalDateTime.now();
        like.modifiedAt = LocalDateTime.now();
        return like;
    }
}

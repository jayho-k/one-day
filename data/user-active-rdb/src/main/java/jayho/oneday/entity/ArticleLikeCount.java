package jayho.oneday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class ArticleLikeCount {

    @Id
    private Long articleId;

    @Setter
    private Long likeCount;

    @Version
    private Long version;

    public static ArticleLikeCount create(Long articleId) {
        ArticleLikeCount articleLikeCount = new ArticleLikeCount();
        articleLikeCount.articleId = articleId;
        articleLikeCount.likeCount = 1L;
        return articleLikeCount;
    }

    public static ArticleLikeCount create(Long articleId, Long likeCount) {
        ArticleLikeCount articleLikeCount = new ArticleLikeCount();
        articleLikeCount.articleId = articleId;
        articleLikeCount.likeCount = likeCount;
        return articleLikeCount;
    }

}

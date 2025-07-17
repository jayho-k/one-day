package jayho.oneday.service.response;

import jakarta.persistence.Id;
import jayho.oneday.entity.ArticleLike;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ArticleLikeResponseData {

    private Long articleId;
    private Long userId;
    private Boolean liked;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ArticleLikeResponseData from (ArticleLike articleLike) {
        ArticleLikeResponseData data = new ArticleLikeResponseData();
        data.articleId = articleLike.getArticleId();
        data.userId = articleLike.getUserId();
        data.liked = articleLike.getLiked();
        data.createdAt = articleLike.getCreatedAt();
        data.modifiedAt = articleLike.getModifiedAt();
        return data;
    }

}

package jayho.oneday.service.response;

import jayho.oneday.entity.ArticleLikeCount;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ArticleLikeCountResponseData {

    private Long articleId;
    private Long count;

    public static ArticleLikeCountResponseData from (ArticleLikeCount articleLikeCount) {
        ArticleLikeCountResponseData data = new ArticleLikeCountResponseData();
        data.articleId = articleLikeCount.getArticleId();
        data.count = articleLikeCount.getLikeCount();
        return data;
    }

}

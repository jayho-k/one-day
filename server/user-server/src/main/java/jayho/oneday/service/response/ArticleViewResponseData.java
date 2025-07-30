package jayho.oneday.service.response;

import jayho.oneday.entity.ArticleViewCount;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ArticleViewResponseData {

    private Long articleId;
    private Long articleViewCount;
    
    public static ArticleViewResponseData from(ArticleViewCount articleViewCount){
            ArticleViewResponseData data = new ArticleViewResponseData();
            data.articleId = articleViewCount.getArticleId();
            data.articleViewCount = articleViewCount.getArticleView();
            return data;
        }
}

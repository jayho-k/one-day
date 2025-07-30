package jayho.oneday.service.response;

import jayho.oneday.entity.Article;
import jayho.oneday.entity.ArticleImage;
import jayho.oneday.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class ArticleResponseData {

    private Long articleId;
    private List<ArticleImage> articleImageList;
    private String content;
    private Long writerId;
    private String writerName;
    private String writerProfileImage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static ArticleResponseData from(Article article, User user, List<ArticleImage> articleImageList) {
        ArticleResponseData data = new ArticleResponseData();
        data.articleId = article.getArticleId();
        data.articleImageList = articleImageList;
        data.content = article.getContent();
        data.writerId = user.getUserId();
        data.writerName = user.getUserName();
        data.writerProfileImage = user.getUserProfileImage();
        data.createdAt = article.getCreatedAt();
        data.modifiedAt = article.getModifiedAt();
        return data;
    }
}

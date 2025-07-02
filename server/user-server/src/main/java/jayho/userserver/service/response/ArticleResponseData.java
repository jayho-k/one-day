package jayho.userserver.service.response;

import jayho.userserver.entity.Article;
import jayho.userserver.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class ArticleResponseData {

    private Long articleId;
    private List<String> images;
    private String content;
    private Long writerId;
    private String writerName;
    private String writerProfileImage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static ArticleResponseData from(Article article, User user) {
        ArticleResponseData data = new ArticleResponseData();
        data.articleId = article.getArticleId();
        data.images = article.getImages();
        data.content = article.getContent();
        data.writerId = user.getUserId();
        data.writerName = user.getUserName();
        data.writerProfileImage = user.getUserProfileImage();
        data.createdAt = article.getCreatedAt();
        data.modifiedAt = article.getModifiedAt();
        return data;
    }
}

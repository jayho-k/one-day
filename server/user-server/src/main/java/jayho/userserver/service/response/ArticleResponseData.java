package jayho.userserver.service.response;

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

    public static ArticleResponseData from(Long articleId) {
        ArticleResponseData article = new ArticleResponseData();
        article.articleId = articleId;
        article.images = List.of("image1.png","image2.png","image3.png");
        article.content = "content";
        article.writerId = articleId;
        article.writerName = "jayho";
        article.writerProfileImage = "image1.png";
        article.createdAt = LocalDateTime.now();
        article.modifiedAt = LocalDateTime.now();
        return article;
    }
}

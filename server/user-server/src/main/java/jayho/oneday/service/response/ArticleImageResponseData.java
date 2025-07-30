package jayho.oneday.service.response;

import jayho.oneday.entity.ArticleImage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleImageResponseData {

    private Long articleImageId;
    private String presignedUrl;
    private String imageName;
    private LocalDateTime createdAt;

    public static ArticleImageResponseData from(ArticleImage articleImage, String presignedUrl) {
        ArticleImageResponseData data = new ArticleImageResponseData();
        data.articleImageId = articleImage.getArticleImageId();
        data.presignedUrl = presignedUrl;
        data.imageName = articleImage.getArticleImageName();
        data.createdAt = articleImage.getCreatedAt();
        return data;
    }

}

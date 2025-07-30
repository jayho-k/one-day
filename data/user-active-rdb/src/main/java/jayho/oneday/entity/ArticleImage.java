package jayho.oneday.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;

@Entity
@BatchSize(size=10)
@Getter
public class ArticleImage {

    @Id
    @NotNull
    private Long articleImageId;

    @Setter
    private Long articleId;

    @NotNull
    private String articleImageName;

    @NotNull
    @Setter
    private Boolean delete;

    private LocalDateTime createdAt;

public static ArticleImage create(Long articleImageId, String articleImageName) {
    ArticleImage articleImage = new ArticleImage();
    articleImage.articleImageId = articleImageId;
    articleImage.articleImageName = articleImageName;
    articleImage.delete = true;
    articleImage.createdAt = LocalDateTime.now();
    return articleImage;
}


}

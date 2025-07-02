package jayho.userserver.entity;

import jakarta.validation.constraints.NotNull;
import jayho.userserver.service.request.ArticleCreateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class Article {

    private Long articleId;
    private List<String> images;
    private String content;
    private Long writerId;
    private Boolean isDelete;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    public static Article create(List<String> images, String content, Long writerId) {
        Article article = new Article();
        article.images = images;
        article.content = content;
        article.writerId = writerId;
        article.isDelete = false;
        article.createdAt = LocalDateTime.now();
        article.modifiedAt = LocalDateTime.now();

        return article;
    }

    public void update(List<String> images, String content) {
        this.images = images;
        this.content = content;
    }

    public void tmpDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public void assignId(Long articleId) {
        this.articleId = articleId;
    }
}

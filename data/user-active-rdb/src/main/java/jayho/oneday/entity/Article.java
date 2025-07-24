package jayho.oneday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article {

    @Id
    @Setter(AccessLevel.NONE)
    private Long articleId;
    private String content;
    private Long writerId;
    private Boolean isDelete;

    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Article create(Long articleId, String content, Long writerId) {
        Article article = new Article();
        article.articleId = articleId;
        article.content = content;
        article.writerId = writerId;
        article.isDelete = false;
        article.createdAt = LocalDateTime.now();
        article.modifiedAt = LocalDateTime.now();
        return article;
    }

}

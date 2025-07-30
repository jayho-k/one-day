package jayho.oneday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class ArticleViewCount {

    @Id
    private Long articleId;
    @Setter
    private Long articleView;

    public static ArticleViewCount create(Long articleId, Long articleView) {
        ArticleViewCount articleViewCount = new ArticleViewCount();
        articleViewCount.articleId = articleId;
        articleViewCount.articleView = articleView;
        return articleViewCount;
    }
}

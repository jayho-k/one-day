package jayho.oneday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SavedArticle {

    @Id
    private Long savedArticleId;
    private Long articleId;
    private Long userId;

    public static SavedArticle create(Long savedArticleId, Long articleId, Long userId) {
        SavedArticle savedArticle = new SavedArticle();
        savedArticle.savedArticleId = savedArticleId;
        savedArticle.articleId = articleId;
        savedArticle.userId = userId;
        return savedArticle;
    }

}

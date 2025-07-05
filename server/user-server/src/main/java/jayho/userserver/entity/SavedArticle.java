package jayho.userserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SavedArticle {

    private Long savedArticleId;
    private Long articleId;
    private Long userId;

    public static SavedArticle create(Long articleId, Long userId) {
        SavedArticle savedArticle = new SavedArticle();
        savedArticle.articleId = articleId;
        savedArticle.userId = userId;
        return savedArticle;
    }

}

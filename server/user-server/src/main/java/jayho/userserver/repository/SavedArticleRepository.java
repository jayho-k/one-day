package jayho.userserver.repository;

import jayho.userserver.entity.Article;
import jayho.userserver.entity.SavedArticle;

import java.util.List;
import java.util.Optional;

public interface SavedArticleRepository {

    void save(SavedArticle savedArticle);
    Optional<SavedArticle> findById(Long savedArticleId);
    List<SavedArticle> findAll(Long userId);
    void deleteById(Long articleId);

}

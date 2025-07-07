package jayho.userserver.repository;

import jayho.userserver.entity.Article;
import jayho.userserver.service.request.ArticleCreateRequest;
import jayho.userserver.service.response.ArticleResponseData;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    Optional<Article> save(Article articleInfo);
    Optional<Article> findById(Long articleId);
    ArticleResponseData findArticleResponseById(Long articleId);
    List<Article> findAll();
    List<ArticleResponseData> findArticleResponseAll(Integer pageSize,  Long lastArticleId);
    Optional<Article> deleteById(Long articleId);

}

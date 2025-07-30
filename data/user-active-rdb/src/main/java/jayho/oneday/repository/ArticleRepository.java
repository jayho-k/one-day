package jayho.oneday.repository;

import jayho.oneday.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findById(Long articleId);
//    Article findArticleResponseById(Long articleId);
//    List<Article> findAll();
//    List<ArticleResponseData> findArticleResponseByPage(Integer pageSize,  Long lastArticleId);
}

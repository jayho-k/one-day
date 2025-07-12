package jayho.useractiverdb.repository;

import jayho.useractiverdb.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findById(Long articleId);
//    Article findArticleResponseById(Long articleId);
//    List<Article> findAll();
//    List<ArticleResponseData> findArticleResponseByPage(Integer pageSize,  Long lastArticleId);
}

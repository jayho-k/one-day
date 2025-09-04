package jayho.oneday.repository;

import jayho.oneday.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findById(Long articleId);

    @Query(
            "select a from Article a where a.articleId in :articleIds"
    )
    List<Article> findByIds(List<Long> articleIds);


//    Article findArticleResponseById(Long articleId);
//    List<Article> findAll();
//    List<ArticleResponseData> findArticleResponseByPage(Integer pageSize,  Long lastArticleId);
}

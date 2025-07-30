package jayho.oneday.repository;

import jayho.oneday.entity.ArticleImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleImageRepository {

    private final ArticleImageJpaRepository articleImageJpaRepository;
    private final ArticleImageJdbcRepository articleImageJdbcRepository;

    public ArticleImage save(ArticleImage articleImage) {
        return articleImageJpaRepository.save(articleImage);
    }

    public List<ArticleImage> saveAll(List<ArticleImage> articleImageList) {
        return articleImageJdbcRepository.saveAll(articleImageList);
    }

    public void updateAll(List<ArticleImage> articleImageList) {
        articleImageJdbcRepository.updateAll(articleImageList);
    }

    public List<ArticleImage> findByArticleId(Long articleId) {
        return articleImageJpaRepository.findByArticleId(articleId);
    }



}

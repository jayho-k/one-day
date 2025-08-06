package jayho.oneday.repository;

import jayho.oneday.entity.ArticleViewCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleViewCountRepository {

    private final ArticleViewCountJpaRepository articleViewCountJpaRepository;
    private final ArticleViewCountJdbcRepository articleViewCountJdbcRepository;

    public void save(ArticleViewCount articleViewCount) {
        articleViewCountJpaRepository.save(articleViewCount);
    }

    public void updateAll(List<ArticleViewCount> articleViewCountList) {
        articleViewCountJdbcRepository.updateAll(articleViewCountList);
    }

    public Optional<ArticleViewCount> findById(Long articleViewCountId) {
        return articleViewCountJpaRepository.findById(articleViewCountId);
    }


}

package jayho.oneday.repository;

import jayho.oneday.entity.ArticleViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleViewCountJpaRepository extends JpaRepository<ArticleViewCount, Long> {
}

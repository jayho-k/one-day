package jayho.oneday.repository;

import jayho.oneday.entity.ArticleViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleViewCountRepository extends JpaRepository<ArticleViewCount, Long> {
}

package jayho.oneday.repository;

import jayho.oneday.entity.ArticleLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long> {

}

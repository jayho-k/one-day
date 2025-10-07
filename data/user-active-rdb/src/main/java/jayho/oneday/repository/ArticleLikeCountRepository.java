package jayho.oneday.repository;

import jayho.oneday.entity.ArticleImage;
import jayho.oneday.entity.ArticleLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long> {

    List<ArticleLikeCount> findByArticleIdIn(List<Long> ids);

}

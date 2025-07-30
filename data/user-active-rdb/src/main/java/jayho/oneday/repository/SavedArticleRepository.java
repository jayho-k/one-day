package jayho.oneday.repository;



import jayho.oneday.entity.SavedArticle;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SavedArticleRepository extends JpaRepository<SavedArticle, Long> {

}

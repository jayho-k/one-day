package jayho.useractiverdb.repository;



import jayho.useractiverdb.entity.SavedArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface SavedArticleRepository extends JpaRepository<SavedArticle, Long> {

}

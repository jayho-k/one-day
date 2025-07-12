package jayho.useractiverdb.repository;

import jayho.useractiverdb.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long articleId);
//    CommentResponseData findCommentResponseById(Long commentId);
//    List<CommentResponseData> findCommentResponseByArticleId(Long articleId, Long lastCommentId, Integer pageSize);

}

package jayho.userserver.repository;

import jayho.userserver.entity.Article;
import jayho.userserver.entity.Comment;
import jayho.userserver.service.response.ArticleResponseData;
import jayho.userserver.service.response.CommentResponseData;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Optional<Comment> save(Comment comment);
    Optional<Comment> findById(Long articleId);
    CommentResponseData findCommentResponseById(Long commentId);
    List<CommentResponseData> findCommentResponseByArticleId(Long articleId, Long lastCommentId, Integer pageSize);
    void deleteById(Long commentId);

}

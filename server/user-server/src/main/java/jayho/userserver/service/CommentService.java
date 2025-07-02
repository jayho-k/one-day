package jayho.userserver.service;


import jakarta.validation.Valid;
import jayho.userserver.entity.Article;
import jayho.userserver.entity.Comment;
import jayho.userserver.repository.CommentRepository;
import jayho.userserver.service.request.ArticleCreateRequest;
import jayho.userserver.service.request.CommentCreateRequest;
import jayho.userserver.service.request.CommentUpdateRequest;
import jayho.userserver.service.response.CommentResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void createComment(CommentCreateRequest commentInfo) {
        commentRepository.save(
                Comment.create(commentInfo.getContent(),
                                commentInfo.getWriterId(),
                                commentInfo.getArticleId()
                )
        );
    }

    public List<CommentResponseData> readAllScroll(Long articleId, Long lastCommentId, Integer pageSize) {
        return commentRepository.findCommentResponseByArticleId(articleId, lastCommentId, pageSize);
    }

    public CommentResponseData updateComment(Long commentId,
                              CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.update(request.getContent());
        commentRepository.save(comment);
        return commentRepository.findCommentResponseById(commentId);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);

    }




}

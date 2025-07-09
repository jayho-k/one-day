package jayho.userserver.service;

import jayho.common.snowflake.Snowflake;
import jayho.useractiverdb.entity.Comment;
import jayho.useractiverdb.entity.User;
import jayho.useractiverdb.repository.CommentRepository;
import jayho.useractiverdb.repository.UserRepository;
import jayho.userserver.service.request.CommentCreateRequest;
import jayho.userserver.service.request.CommentUpdateRequest;
import jayho.userserver.service.response.CommentResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final Snowflake snowflake = new Snowflake();

    public Comment createComment(CommentCreateRequest commentInfo) {
        return commentRepository.save(
                Comment.create(snowflake.nextId(),
                                commentInfo.getContent(),
                                commentInfo.getWriterId(),
                                commentInfo.getArticleId()
                )
        );
    }

    public List<CommentResponseData> readAllScroll(Long articleId, Long lastCommentId, Integer pageSize) {
//        return commentRepository.findCommentResponseByArticleId(articleId, lastCommentId, pageSize);
        return List.of();
    }

    public CommentResponseData updateComment(Long commentId, CommentUpdateRequest commentInfo) {

        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setContent(commentInfo.getContent());

        Comment updateComment = commentRepository.save(comment);
        User user = userRepository.findById(updateComment.getWriterId()).orElseThrow();
        return CommentResponseData.from(updateComment, user);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);

    }
}

package jayho.oneday.service;

import jayho.common.snowflake.Snowflake;
import jayho.oneday.entity.Comment;
import jayho.oneday.entity.User;
import jayho.oneday.repository.CommentRepository;
import jayho.oneday.repository.UserRepository;
import jayho.oneday.service.request.CommentCreateRequest;
import jayho.oneday.service.request.CommentUpdateRequest;
import jayho.oneday.service.response.CommentResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final Snowflake snowflake;

    @Transactional
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

    @Transactional
    public CommentResponseData updateComment(Long commentId, CommentUpdateRequest commentInfo) {

        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setContent(commentInfo.getContent());

        Comment updateComment = commentRepository.save(comment);
        User user = userRepository.findById(updateComment.getWriterId()).orElseThrow();
        return CommentResponseData.from(updateComment, user);
    }

    @Transactional
    public Long deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
        return commentId;
    }
}

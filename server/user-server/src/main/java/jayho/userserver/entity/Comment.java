package jayho.userserver.entity;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Comment {

    private Long commentId;
    private String content;
    private Long writerId;
    private Long articleId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Comment create(String content, Long writerId, Long articleId) {
        Comment comment = new Comment();
        comment.content = content;
        comment.writerId = writerId;
        comment.articleId = articleId;
        comment.createdAt = LocalDateTime.now();
        comment.modifiedAt = LocalDateTime.now();
        return comment;
    }

    public void update(String content) {
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }


    public void assignId(Long commentId) {
        this.commentId = commentId;
    }
}

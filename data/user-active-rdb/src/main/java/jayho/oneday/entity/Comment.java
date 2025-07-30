package jayho.oneday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @Setter(AccessLevel.NONE)
    private Long commentId;
    private String content;
    @Setter(AccessLevel.NONE)
    private Long writerId;
    @Setter(AccessLevel.NONE)
    private Long articleId;
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Comment create(Long commentId ,String content, Long writerId, Long articleId) {
        Comment comment = new Comment();
        comment.commentId = commentId;
        comment.content = content;
        comment.writerId = writerId;
        comment.articleId = articleId;
        comment.createdAt = LocalDateTime.now();
        comment.modifiedAt = LocalDateTime.now();
        return comment;
    }
}

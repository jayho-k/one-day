package jayho.userserver.service.response;

import jayho.userserver.entity.Comment;
import jayho.userserver.entity.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommentResponseData {

    private Long commentId;
    private String content;
    private Long writerId;
    private String writerName;
    private String writerProfileImage;
    private Long articleId;

    public static CommentResponseData from(Comment comment, User writer) {

        CommentResponseData response = new CommentResponseData();
        response.commentId = comment.getCommentId();
        response.content = comment.getContent();
        response.writerId = comment.getWriterId();
        response.writerName = writer.getUserName();
        response.writerProfileImage = writer.getUserProfileImage();
        response.articleId = comment.getArticleId();

        return response;
    }



}

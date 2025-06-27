package jayho.userserver.service.response;

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

    public static CommentResponseData from(Long commentId) {

        CommentResponseData response = new CommentResponseData();
        response.commentId = commentId;
        response.content = "content";
        response.writerId = 1L;
        response.writerName = "writer";
        response.writerProfileImage = "image.png";
        response.articleId = 1L;

        return response;
    }



}

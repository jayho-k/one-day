package jayho.userserver.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentCreateRequest {

    @NotNull
    private Long articleId;

    @NotNull
    private String content;

    @NotNull
    private Long writerId;


}

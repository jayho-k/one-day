package jayho.oneday.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    @NotNull
    private Long articleId;

    @NotNull
    private String content;

    @NotNull
    private Long writerId;

}

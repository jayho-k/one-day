package jayho.oneday.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateRequest {

    @NotNull
    private Long articleId;

    @NotNull
    private String content;

    @NotNull
    private Long writerId;


}

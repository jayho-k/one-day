package jayho.oneday.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ArticleSaveRequest {

    @NotNull
    private Long userId;

}

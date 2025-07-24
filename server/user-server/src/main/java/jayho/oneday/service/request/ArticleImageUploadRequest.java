package jayho.oneday.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleImageUploadRequest {

    @NotNull
    private Long userId; // tmp
    @NotNull
    private String articleImageName;

}

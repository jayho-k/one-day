package jayho.oneday.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleUpdateRequest {

    @NotNull
    private Long articleId;
    private List<String> images;

    @NotNull
    private String content;

}

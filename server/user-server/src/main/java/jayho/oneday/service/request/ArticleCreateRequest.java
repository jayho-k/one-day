package jayho.oneday.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleCreateRequest {

    @NotNull
    private List<String> images;
    private String content;

    @NotNull
    private Long writerId;

}

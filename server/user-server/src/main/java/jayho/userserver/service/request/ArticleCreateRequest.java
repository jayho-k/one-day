package jayho.userserver.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class ArticleCreateRequest {

    @NotNull
    private List<String> images;
    private String content;

    @NotNull
    private Long writerId;

}

package jayho.oneday.service.request;

import jakarta.validation.constraints.NotNull;
import jayho.oneday.entity.ArticleImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleCreateRequest {

    @NotNull
    private List<ArticleImage> images;
    private String content;

    @NotNull
    private Long writerId;

}

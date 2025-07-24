package jayho.oneday.service.request;

import jakarta.validation.constraints.NotNull;
import jayho.oneday.entity.ArticleImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleUpdateRequest {

    @NotNull
    private Long articleId;
    private List<ArticleImage> images;

    @NotNull
    private String content;

}

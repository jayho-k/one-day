package jayho.oneday.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class ArticleViewEvent implements Serializable {

    private Long articleId;
    private Long userId;

    public static ArticleViewEvent create(Long articleId, Long userId) {
        ArticleViewEvent articleViewCount =  new ArticleViewEvent();
        articleViewCount.articleId = articleId;
        articleViewCount.userId = userId;
        return articleViewCount;
    }

}

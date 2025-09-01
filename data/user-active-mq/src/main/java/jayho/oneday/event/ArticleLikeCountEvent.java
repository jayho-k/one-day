package jayho.oneday.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class ArticleLikeCountEvent implements Serializable {

    private Long articleId;
    private Long count;
    private Boolean like;

    public static ArticleLikeCountEvent create(Long articleId, Long count, Boolean like) {
        ArticleLikeCountEvent articleLikeCountEvent =  new ArticleLikeCountEvent();
        articleLikeCountEvent.articleId = articleId;
        articleLikeCountEvent.count = count;
        articleLikeCountEvent.like = like;
        return articleLikeCountEvent;
    }

}

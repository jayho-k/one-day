package jayho.oneday.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class ArticleLikeEvent implements Serializable {

    private Long articleId;
    private Long userId;
    private Boolean like;

    public static ArticleLikeEvent create(Long articleId, Long userId, Boolean like) {
        ArticleLikeEvent articleLikeEvent =  new ArticleLikeEvent();
        articleLikeEvent.articleId = articleId;
        articleLikeEvent.userId = userId;
        articleLikeEvent.like = like;
        return articleLikeEvent;
    }

}

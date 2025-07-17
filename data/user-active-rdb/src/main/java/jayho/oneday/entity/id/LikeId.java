package jayho.oneday.entity.id;

import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LikeId implements Serializable {
    private Long articleId;
    private Long userId;

    public static LikeId create(Long articleId, Long userId) {
        LikeId likeId = new LikeId();
        likeId.articleId = articleId;
        likeId.userId = userId;
        return likeId;
    }

}

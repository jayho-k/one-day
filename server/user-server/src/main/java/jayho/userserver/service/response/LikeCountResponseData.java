package jayho.userserver.service.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LikeCountResponseData {

    private Long articleId;
    private Long count;

    public static LikeCountResponseData from (Long articleId) {
        LikeCountResponseData data = new LikeCountResponseData();
        data.articleId = 1L;
        data.count = 12345L;
        return data;
    }

}

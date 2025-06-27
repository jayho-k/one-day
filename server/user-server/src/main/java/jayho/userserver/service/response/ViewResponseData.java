package jayho.userserver.service.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ViewResponseData {

    private Long viewCount;

    public static ViewResponseData from(){
        ViewResponseData responseData = new ViewResponseData();
        responseData.viewCount = 123L;
        return responseData;
    }
}

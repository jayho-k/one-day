package jayho.userserver.service.response;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class BaseResponseWithData extends BaseResponse{

    protected Object data;

    public static BaseResponseWithData from(int status, String message, Object data) {
        BaseResponseWithData response = new BaseResponseWithData();
        response.status = status;
        response.message = message;
        response.data = data;
        return response;
    }
}


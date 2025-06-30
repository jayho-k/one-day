package jayho.userserver.service.response;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class BaseResponseWithData<T> extends BaseResponse{

    protected T data;

    public static <T> BaseResponseWithData<T> from(int status, String message, T data) {
        BaseResponseWithData response = new BaseResponseWithData();
        response.status = status;
        response.message = message;
        response.data = data;
        return response;
    }
}


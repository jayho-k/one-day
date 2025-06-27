package jayho.userserver.service.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {

    protected int status;
    protected String message;

    public static BaseResponse from(int status, String message){
        BaseResponse response = new BaseResponse();
        response.status = status;
        response.message = message;
        return response;
    }

}



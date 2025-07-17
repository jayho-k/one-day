package jayho.oneday.service.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private int status;

    @JsonInclude(Include.NON_NULL)
    private T data;


    public static <T> BaseResponse<T> from(int status){
        BaseResponse<T> response = new BaseResponse<>();
        response.status = status;
        return response;
    }

    public static <T> BaseResponse<T> from(int status, T data){
        BaseResponse<T> response = new BaseResponse<>();
        response.status = status;
        response.data = data;
        return response;
    }

}



package jayho.userserver.controller.exception;

import jayho.userserver.service.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<BaseResponse> handleCustomException(ApiException e) {
        ExceptionInfo exceptionInfo = e.getExceptionInfo();
        return ResponseEntity
                .status(exceptionInfo.getStatus())
                .body(BaseResponse.from(
                        exceptionInfo.getStatus().value(),
                        exceptionInfo.getMessage())
                );
    }

}

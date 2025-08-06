package jayho.oneday.controller.exception;

import jayho.oneday.service.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@Slf4j
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

    @ExceptionHandler(AbusingException.class)
    public void handleCustomException(AbusingException e) {
        log.info(e.getMessage());
    }
}

package jayho.oneday.controller.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException{

    private final ExceptionInfo exceptionInfo;

}

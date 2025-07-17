package jayho.oneday.controller.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionInfo {

    String name();
    HttpStatus getStatus();
    String getMessage();

}

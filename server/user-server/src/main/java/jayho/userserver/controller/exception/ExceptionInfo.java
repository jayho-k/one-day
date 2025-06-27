package jayho.userserver.controller.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionInfo {

    String name();
    HttpStatus getStatus();
    String getMessage();

}

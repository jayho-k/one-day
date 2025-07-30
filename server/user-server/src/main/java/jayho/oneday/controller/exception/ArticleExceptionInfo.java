package jayho.oneday.controller.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ArticleExceptionInfo implements ExceptionInfo {

    NOT_FOUND_ARTICLE(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String message;

}

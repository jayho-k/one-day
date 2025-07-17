package jayho.oneday.controller;

import jayho.oneday.service.ViewService;
import jayho.oneday.service.response.ArticleViewResponseData;
import jayho.oneday.service.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ViewController {

    private final ViewService viewService;

    @PostMapping("/view-count/article/{articleId}/user/{userId}")
    public ResponseEntity<BaseResponse<ArticleViewResponseData>> increaseViewCount(@PathVariable("articleId") Long articleId,
                                                                                   @PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        viewService.increaseViewCount(articleId, userId))
                );
    }

    @GetMapping("/view-count/article/{articleId}")
    public ResponseEntity<BaseResponse> readViewCount(@PathVariable Long articleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        viewService.readViewCount(articleId))
                );
    }


}

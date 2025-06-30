package jayho.userserver.controller;

import jakarta.validation.Valid;
import jayho.userserver.service.request.ArticleCreateRequest;
import jayho.userserver.service.response.ArticleResponseData;
import jayho.userserver.service.response.BaseResponse;
import jayho.userserver.service.response.BaseResponseWithData;
import jayho.userserver.service.response.ViewResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ViewController {

    @PostMapping("/view-count/article/{articleId}/user/{userId}")
    public ResponseEntity<BaseResponse> increaseViewCount(@PathVariable("articleId") Long articleId,
                                                          @PathVariable("userId") Long userId) {
        // new ApiException(NOT_FOUND_ARTICLE);
        // new NotFountArticleException();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        "해당 게시글의 조회수가 증가했습니다.")
                );
    }

    @GetMapping("/view-count/article/{articleId}")
    public ResponseEntity<BaseResponseWithData> readViewCount(@PathVariable Long articleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponseWithData.from(
                        200,
                        "해당 게시글의 조회수가 조회 되었습니다.",
                        ViewResponseData.from())
                );
    }


}

package jayho.userserver.controller;

import jayho.userserver.service.response.BaseResponse;
import jayho.userserver.service.response.LikeCountResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LikeController {

    @PostMapping("/article-likes/article/{articleId}/user/{userId}")
    public ResponseEntity<BaseResponse> createLike(@PathVariable Long articleId,
                                                   @PathVariable Long userId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.from(
                        201,
                        "좋아요 생성에 성공했습니다."
                ));
    }

    @DeleteMapping("/article-likes/article/{articleId}/user/{userId}")
    public ResponseEntity<BaseResponse> deleteLike(@PathVariable Long articleId,
                                                   @PathVariable Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        "좋아요 삭제에 성공했습니다."
                ));
    }

    @GetMapping("/article-likes/article/{articleId}/count")
    public ResponseEntity<BaseResponse> getLikeCount(@PathVariable Long articleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        LikeCountResponseData.from(articleId)
                ));
    }

}




package jayho.oneday.controller;

import jayho.oneday.entity.ArticleLike;
import jayho.oneday.service.ArticleLikeService;
import jayho.oneday.service.response.ArticleLikeResponseData;
import jayho.oneday.service.response.BaseResponse;
import jayho.oneday.service.response.ArticleLikeCountResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping("/article-likes/article/{articleId}/user/{userId}")
    public ResponseEntity<BaseResponse<ArticleLikeResponseData>> articleLike(@PathVariable Long articleId,
                                                                             @PathVariable Long userId) {
        articleLikeService.articleLikeToMQ(articleId, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.from(
                        201
                ));
    }

    @PutMapping("/article-likes/article/{articleId}/user/{userId}")
    public ResponseEntity<BaseResponse<ArticleLikeResponseData>> articleUnlike(@PathVariable Long articleId,
                                                                               @PathVariable Long userId) {
        articleLikeService.articleUnlikeMQ(articleId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(200));
    }

    @GetMapping("/article-likes/article/{articleId}/count")
    public ResponseEntity<BaseResponse<ArticleLikeCountResponseData>> readLikeCount(@PathVariable Long articleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        articleLikeService.readLikeCount(articleId)
                ));
    }

}




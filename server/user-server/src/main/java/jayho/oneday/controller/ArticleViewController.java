package jayho.oneday.controller;

import jayho.oneday.entity.ArticleViewCount;
import jayho.oneday.service.ArticleViewService;
import jayho.oneday.service.response.ArticleViewResponseData;
import jayho.oneday.service.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleViewController {

    private final ArticleViewService articleViewService;

    @PostMapping("/view-count/article/{articleId}/user/{userId}")
    public ResponseEntity<BaseResponse<ArticleViewResponseData>> increaseViewCount(@PathVariable("articleId") Long articleId,
                                                                                   @PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        articleViewService.increaseViewCount(articleId, userId))
                );
    }

    @GetMapping("/view-count/article/{articleId}")
    public ResponseEntity<BaseResponse<ArticleViewCount>> readViewCount(@PathVariable Long articleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        articleViewService.readViewCount(articleId))
                );
    }


}

package jayho.userserver.controller;

import jakarta.validation.Valid;
import jayho.useractiverdb.entity.Article;
import jayho.useractiverdb.entity.SavedArticle;
import jayho.userserver.service.ArticleService;
import jayho.userserver.service.request.ArticleCreateRequest;
import jayho.userserver.service.request.ArticleUpdateRequest;
import jayho.userserver.service.response.ArticleResponseData;
import jayho.userserver.service.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/article")
    public ResponseEntity<BaseResponse<Article>> createArticle(@RequestBody @Valid ArticleCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.from(
                        201,
                        articleService.createArticle(request)));
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<BaseResponse<ArticleResponseData>> readArticle(@PathVariable("articleId") Long articleId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200
                        , articleService.readArticle(articleId))
                );
    }

    @GetMapping("/article-all")
    public ResponseEntity<BaseResponse<List<ArticleResponseData>>> readAllArticle(@RequestParam("pageSize") Integer pageSize,
                                                                                  @RequestParam(value = "lastArticleId", required = false) Long lastArticleId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200
                        , articleService.readAllArticle(pageSize, lastArticleId)));
    }

    @PutMapping("/article")
    public ResponseEntity<BaseResponse<ArticleResponseData>> updateArticle(@RequestBody @Valid ArticleUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200
                        ,articleService.updateArticle(request)
                ));
    }

    @PutMapping("/article/temp-delete/{articleId}")
    public ResponseEntity<BaseResponse<Article>> tempDeleteArticle(@PathVariable("articleId") Long articleId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        articleService.tmpDeleteArticle(articleId)));
    }

    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<BaseResponse<Long>> deleteArticle(@PathVariable("articleId") Long articleId) throws Exception{
        articleService.deleteArticle(articleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(200));
    }

    @PostMapping("/article/{articleId}/user/{userId}/save")
    public ResponseEntity<BaseResponse<SavedArticle>> saveCollectArticle(@PathVariable("articleId") Long articleId,
                                                                         @PathVariable("userId") Long userId) throws Exception{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        articleService.saveCollectArticle(articleId, userId)));
    }
}

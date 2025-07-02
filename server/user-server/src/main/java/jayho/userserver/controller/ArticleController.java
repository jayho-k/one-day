package jayho.userserver.controller;

import jakarta.validation.Valid;
import jayho.userserver.repository.ArticleRepositoryImpl;
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
    public ResponseEntity<BaseResponse> createArticle(@RequestBody @Valid ArticleCreateRequest request) {
        articleService.createArticle(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.from(201));
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<BaseResponse> readArticle(@PathVariable("articleId") Long articleId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200
                        , articleService.readArticle(articleId))
                );
    }

    @GetMapping("/article")
    public ResponseEntity<BaseResponse<List<ArticleResponseData>>> readAllArticle(@RequestParam("pageSize") Integer pageSize,
                                                                                  @RequestParam(value = "lastArticleId", required = false) Long lastArticleId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200
                        , articleService.readAllArticle(pageSize, lastArticleId)));
    }

    @PutMapping("/article")
    public ResponseEntity<BaseResponse<ArticleResponseData>> updateArticle(@RequestBody ArticleUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200
                        ,articleService.updateArticle(request)
                ));
    }

    @PutMapping("/article/temp-delete/{articleId}")
    public ResponseEntity<BaseResponse> tempDeleteArticle(@PathVariable("articleId") Long articleId){
        articleService.tmpDeleteArticle(articleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(200));
    }

    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<BaseResponse> deleteArticle(@PathVariable("articleId") Long articleId) throws Exception{

        articleService.deleteArticle(articleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(200));
    }

    @PostMapping("/article/{articleId}/user/{userId}/save")
    public ResponseEntity<BaseResponse> saveArticle(@PathVariable("articleId") Long articleId,
                                                    @PathVariable("userId") Long userId) throws Exception{

        articleService.saveArticle(articleId, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(200));
    }
}

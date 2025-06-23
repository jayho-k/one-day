package jayho.userserver.controller;

import jakarta.validation.Valid;
import jayho.userserver.service.request.ArticleCreateRequest;
import jayho.userserver.service.request.ArticleSaveRequest;
import jayho.userserver.service.response.ArticleResponseData;
import jayho.userserver.service.response.BaseResponse;
import jayho.userserver.service.response.BaseResponseWithData;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.IntStream;


@RestController
public class ArticleController {

    @PostMapping("/article")
    public ResponseEntity<BaseResponse> createArticle(@RequestBody @Valid ArticleCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponseWithData.from(
                        201,
                        "게시글이 생성되었습니다.",
                        ArticleResponseData.from(request.getWriterId()))
                );
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<BaseResponse> readArticle(@PathVariable("articleId") Long articleId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponseWithData.from(
                        200
                        , "게시글 조회가 성공했습니다."
                        , ArticleResponseData.from(articleId))
                );
    }

    @GetMapping("/article")
    public ResponseEntity<BaseResponseWithData> readAllArticle(@RequestParam("pageSize") Integer pageSize,
                                                               @RequestParam(value = "lastArticleId", required = false) Long lastArticleId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponseWithData.from(
                        200
                        , "게시글 리스트 조회가 성공했습니다."
                        , IntStream.range(0, 3)
                                .mapToObj(i -> ArticleResponseData.from((long) i))
                                .toList()));
    }

    @PatchMapping("/article/{articleId}")
    public ResponseEntity<BaseResponseWithData> updateArticle(@PathVariable("articleId") Long articleId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponseWithData.from(
                        200
                        , "게시글 수정에 성공했습니다."
                        , ArticleResponseData.from(articleId)
                ));
    }

    @PatchMapping("/article/temp-delete/{articleId}")
    public ResponseEntity<BaseResponse> tempDeleteArticle(@PathVariable("articleId") Long articleId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200
                        , "게시글 임시 삭제에 성공했습니다. delete가 true로 변경 되었습니다."
                ));
    }

    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<BaseResponse> deleteArticle(@PathVariable("articleId") Long articleId) throws Exception{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200
                        , "게시글 삭제에 성공했습니다."
                ));
    }

    @PostMapping("/article/{articleId}/save")
    public ResponseEntity<BaseResponse> saveArticle(@PathVariable("articleId") Long articleId,
                                                    @RequestBody @Valid ArticleSaveRequest request) throws Exception{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200
                        , "게시글 저장에 성공했습니다."
                ));
    }
}

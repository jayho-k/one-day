package jayho.userserver.controller;

import jakarta.validation.Valid;
import jayho.userserver.service.request.CommentCreateRequest;
import jayho.userserver.service.request.CommentUpdateRequest;
import jayho.userserver.service.response.BaseResponse;
import jayho.userserver.service.response.BaseResponseWithData;
import jayho.userserver.service.response.CommentResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    @PostMapping("/comment")
    public ResponseEntity<BaseResponse> createComment(@RequestBody @Valid CommentCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.from(
                        201,
                        "댓글이 생성되었습니다."
                ));
    }

    @GetMapping("/comment/scroll")
    public ResponseEntity<BaseResponseWithData> readAllScroll(@RequestParam("articleId") Long articleId,
                                                               @RequestParam("lastCommentId") Long lastCommentId,
                                                               @RequestParam("pageSize") Integer pageSize) {
        List<CommentResponseData> data = List.of(
                CommentResponseData.from(1L),
                CommentResponseData.from(2L),
                CommentResponseData.from(3L));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponseWithData.from(
                        200,
                        "댓글 조회에 성공했습니다.",
                        data
                ));
    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<BaseResponse> updateComment(@PathVariable Long commentId,
                                                      @RequestBody @Valid CommentUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        "댓글 수정에 성공했습니다."
                ));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<BaseResponse> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        "댓글 삭제가 성공했습니다."
                ));
    }

}

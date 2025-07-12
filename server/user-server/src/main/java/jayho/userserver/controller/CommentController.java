package jayho.userserver.controller;

import jakarta.validation.Valid;
import jayho.useractiverdb.entity.Comment;
import jayho.userserver.service.CommentService;
import jayho.userserver.service.request.CommentCreateRequest;
import jayho.userserver.service.request.CommentUpdateRequest;
import jayho.userserver.service.response.BaseResponse;
import jayho.userserver.service.response.CommentResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<BaseResponse<Comment>> createComment(@RequestBody @Valid CommentCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.from(
                        201,
                        commentService.createComment(request)
                ));
    }

    @GetMapping("/comment/scroll")
    public ResponseEntity<BaseResponse<List<CommentResponseData>>> readAllScroll(@RequestParam("articleId") Long articleId,
                                                                                 @RequestParam("lastCommentId") Long lastCommentId,
                                                                                 @RequestParam("pageSize") Integer pageSize) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        commentService.readAllScroll(articleId, lastCommentId, pageSize)));
    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<BaseResponse<CommentResponseData>> updateComment(@PathVariable Long commentId,
                                                                           @RequestBody @Valid CommentUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        commentService.updateComment(commentId, request)
                ));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<BaseResponse<Long>> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        commentService.deleteComment(commentId)
                ));
    }

}

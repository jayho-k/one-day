package jayho.userserver.controller;

import jayho.userserver.service.response.BaseResponse;
import jayho.userserver.service.response.ViewResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ViewController {

    @PostMapping("/view-count/article/{articleId}/user/{userId}")
    public ResponseEntity<BaseResponse> increaseViewCount(@PathVariable("articleId") Long articleId,
                                                          @PathVariable("userId") Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(200)
                );
    }

    @GetMapping("/view-count/article/{articleId}")
    public ResponseEntity<BaseResponse> readViewCount(@PathVariable Long articleId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.from(
                        200,
                        ViewResponseData.from())
                );
    }


}

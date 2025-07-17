package jayho.oneday.api;

import jayho.oneday.service.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CommentTest {

    @LocalServerPort
    int port;
    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create(String.format("http://localhost:%s",port));
    }

    @DisplayName("댓글 생성 성공 테스트")
    @Test
    void createCommentTest() {
        Long articleId = 1L;
        String content = "content";
        Long writerId = 1L;

        ResponseEntity<BaseResponse> res1 = createComment(CommentCreateRequest.builder()
                .articleId(articleId)
                .content(content)
                .writerId(writerId)
                .build());

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @DisplayName("댓글 생성 시 articleId null BAD_REQUEST 테스트")
    @Test
    void createComment_articleIdNullTest() {
        Long articleId = 1L;
        String content = "content";
        Long writerId = 1L;

        ResponseEntity<BaseResponse> res2 = createComment4xx(CommentCreateRequest.builder()
                .articleId(null)
                .content(content)
                .writerId(writerId)
                .build());

        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("댓글 생성 시 content null BAD_REQUEST 테스트")
    @Test
    void createComment_writerIdNullTest() {
        Long articleId = 1L;
        String content = "content";
        Long writerId = 1L;

        ResponseEntity<BaseResponse> res3 = createComment4xx(CommentCreateRequest.builder()
                .articleId(articleId)
                .content(content)
                .writerId(null)
                .build());
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<BaseResponse> createComment(CommentCreateRequest request){
        return restClient.post()
                .uri("/comment")
                .body(request)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> createComment4xx(CommentCreateRequest request){
        return restClient.post()
                .uri("/comment")
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long writerId;
    }


    @Test
    void readAllScrollTest() {

        Long articleId = 1L;
        Long lastCommentId = 1L;
        Integer pageSize = 10;

        ResponseEntity<BaseResponse> res1 = readAllScroll(articleId, lastCommentId, pageSize);
        ResponseEntity<BaseResponse> res2 = readAllScroll4xx(null, lastCommentId, pageSize);
        ResponseEntity<BaseResponse> res3 = readAllScroll4xx(articleId, null, pageSize);
        ResponseEntity<BaseResponse> res4 = readAllScroll4xx(articleId, lastCommentId, null);

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res4.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<BaseResponse> readAllScroll(Long articleId, Long lastCommentId, Integer pageSize) {
        return restClient.get()
                .uri("/comment/scroll?articleId={articleId}&lastCommentId={lastCommentId}&pageSize={pageSize}", articleId, lastCommentId, pageSize)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> readAllScroll4xx(Long articleId, Long lastCommentId, Integer pageSize) {
        return restClient.get()
                .uri("/comment/scroll?articleId={articleId}&lastCommentId={lastCommentId}&pageSize={pageSize}", articleId, lastCommentId, pageSize)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @DisplayName("댓글 수정 성공 테스트")
    @Test
    void updateCommentTest(){
        Long commentId = 1L;
        Long articleId = 1L;
        String content = "content";
        Long writerId = 1L;

        ResponseEntity<BaseResponse> res1 = updateComment(commentId, CommentUpdateRequest.builder()
                .articleId(articleId)
                .content(content)
                .writerId(writerId)
                .build()
        );
        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("댓글 수정 시 commentId null NOT_FOUND 테스트")
    @Test
    void updateComment_commentIdNullTest(){
        Long commentId = 1L;
        Long articleId = 1L;
        String content = "content";
        Long writerId = 1L;

        ResponseEntity<BaseResponse> res2 = updateComment4xx(null, CommentUpdateRequest.builder()
                .articleId(articleId)
                .content(content)
                .writerId(writerId)
                .build()
        );
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @DisplayName("댓글 수정 시 articleId null BAD_REQUEST 테스트")
    @Test
    void updateComment_articleIdNullTest(){
        Long commentId = 1L;
        Long articleId = 1L;
        String content = "content";
        Long writerId = 1L;


        ResponseEntity<BaseResponse> res3 = updateComment4xx(commentId, CommentUpdateRequest.builder()
                .articleId(null)
                .content(content)
                .writerId(writerId)
                .build()
        );
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("댓글 수정 시 writerId null BAD_REQUEST 테스트")
    @Test
    void updateComment_writerIdNullTest(){
        Long commentId = 1L;
        Long articleId = 1L;
        String content = "content";
        Long writerId = 1L;

        ResponseEntity<BaseResponse> res4 = updateComment4xx(commentId, CommentUpdateRequest.builder()
                .articleId(articleId)
                .content(content)
                .writerId(null)
                .build()
        );
        assertThat(res4.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<BaseResponse> updateComment(Long commentId, CommentUpdateRequest request) {
        return restClient.patch()
                .uri("/comment/{commentId}", commentId)
                .body(request)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> updateComment4xx(Long commentId, CommentUpdateRequest request) {
        return restClient.patch()
                .uri("/comment/{commentId}", commentId)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static class CommentUpdateRequest {
        private Long articleId;
        private String content;
        private Long writerId;
    }

    @DisplayName("댓글 삭제 성공 테스트")
    @Test
    void deleteCommentTest() {
        Long commentId = 1L;
        ResponseEntity<BaseResponse> res1 = deleteComment(commentId);
        Assertions.assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("댓글 삭제 시 commentId null NOT_FOUND 테스트")
    @Test
    void deleteComment_commentIdNullTest() {
        Long commentId = 1L;

        ResponseEntity<BaseResponse> res2 = deleteComment4xx(null);
        Assertions.assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    ResponseEntity<BaseResponse> deleteComment(Long commentId) {
        return restClient.delete()
                .uri("/comment/{commentId}", commentId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }

    ResponseEntity<BaseResponse> deleteComment4xx(Long commentId) {
        return restClient.delete()
                .uri("/comment/{commentId}", commentId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

}

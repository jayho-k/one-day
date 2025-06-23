package jayho.userserver.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jayho.userserver.controller.exception.ApiException;
import jayho.userserver.controller.exception.ArticleExceptionInfo;
import jayho.userserver.service.response.BaseResponse;
import jayho.userserver.service.response.BaseResponseWithData;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ArticleTest {

    @LocalServerPort
    int port;
    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create(String.format("http://localhost:%s",port));
    }

    @Test
    void createArticleTest() {
        Long articleId = 1L;
        List<String> images = List.of("image1.png", "image2.png", "image3.png");
        String content = "content";

        ResponseEntity<BaseResponse> res1 = createArticle(ArticleCreateRequest.builder()
                .images(images)
                .content(content)
                .writerId(articleId)
                .build());

        ResponseEntity<BaseResponse> res2 = createArticle4xx(ArticleCreateRequest.builder()
                .images(null)
                .content(content)
                .writerId(articleId)
                .build());

        ResponseEntity<BaseResponse> res3 = createArticle4xx(ArticleCreateRequest.builder()
                .images(images)
                .content(content)
                .writerId(null)
                .build());

        assertThat(HttpStatus.CREATED).isEqualTo(res1.getStatusCode());
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(res2.getStatusCode());
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(res3.getStatusCode());
    }

    ResponseEntity<BaseResponse> createArticle(ArticleCreateRequest request){
        return restClient.post()
                .uri("/article")
                .body(request)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> createArticle4xx(ArticleCreateRequest request){
        return restClient.post()
                .uri("/article")
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static class ArticleCreateRequest{
        private List<String> images;
        private String content;
        private Long writerId;
    }

    @Test
    void readArticleTest() {
        ResponseEntity<BaseResponseWithData> response = readArticle(1L);

        System.out.println(response);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    }

    ResponseEntity<BaseResponseWithData> readArticle(Long articleId) {
        return restClient.get()
                .uri("/article/{articleId}", articleId)
                .retrieve()
                .toEntity(BaseResponseWithData.class);
    }

    @DisplayName("read all article 테스트")
    @Test
    void readAllArticleTest() {
        ResponseEntity<BaseResponseWithData> res1 = readAllArticle(1, 2L);
        ResponseEntity<BaseResponseWithData> res2 = readAllArticleFirstPage(1, null);
        ResponseEntity<BaseResponse> res3 = readAllArticleFirstPage4xx(null, null);

        System.out.println(res1);
        assertThat(HttpStatus.OK).isEqualTo(res1.getStatusCode());

        System.out.println(res2);
        assertThat(HttpStatus.OK).isEqualTo(res2.getStatusCode());

        System.out.println(res3);
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(res3.getStatusCode());
    }

    ResponseEntity<BaseResponseWithData> readAllArticle(Integer pageSize, Long lastArticleId) {
        return restClient.get()
                .uri("/article?pageSize={pageSize}&lastArticleId={lastArticleId}", pageSize, lastArticleId)
                .retrieve()
                .toEntity(BaseResponseWithData.class);
    }
    ResponseEntity<BaseResponseWithData> readAllArticleFirstPage(Integer pageSize, Long lastArticleId) {
        return restClient.get()
                .uri("/article?pageSize={pageSize}", pageSize)
                .retrieve()
                .toEntity(BaseResponseWithData.class);
    }
    ResponseEntity<BaseResponse> readAllArticleFirstPage4xx(Integer pageSize, Long lastArticleId) {
        return restClient.get()
                .uri("/article?pageSize={pageSize}", pageSize)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
                .toEntity(BaseResponse.class);
    }


    @Test
    void updateArticleTest(){
        ResponseEntity<BaseResponseWithData> res1 = updateArticle(1L);
        ResponseEntity<BaseResponseWithData> res2 = updateArticle4xx(null);

        System.out.println(res1);
        assertThat(HttpStatus.OK).isEqualTo(res1.getStatusCode());

        System.out.println(res2);
        assertThat(HttpStatus.NOT_FOUND).isEqualTo(res2.getStatusCode());
    }

    ResponseEntity<BaseResponseWithData> updateArticle(Long articleId) {
        return restClient.patch()
                .uri("/article/{articleId}", articleId)
                .retrieve()
                .toEntity(BaseResponseWithData.class);
    }
    ResponseEntity<BaseResponseWithData> updateArticle4xx(Long articleId) {
        return restClient.patch()
                .uri("/article/{articleId}", articleId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponseWithData.class);
    }

    @Test
    void tempDeleteArticleTest() {
        ResponseEntity<BaseResponse> res1 = tempDeleteArticle(1L);
        ResponseEntity<BaseResponse> res2 = tempDeleteArticle4xx(null);

        System.out.println(res1);
        assertThat(HttpStatus.OK).isEqualTo(res1.getStatusCode());

        System.out.println(res2);
        assertThat(HttpStatus.NOT_FOUND).isEqualTo(res2.getStatusCode());
    }

    ResponseEntity<BaseResponse> tempDeleteArticle(Long articleId) {
        return restClient.patch()
                .uri("/article/temp-delete/{articleId}", articleId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> tempDeleteArticle4xx(Long articleId) {
        return restClient.patch()
                .uri("/article/temp-delete/{articleId}", articleId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Test
    void saveArticleTest() {
        ResponseEntity<BaseResponse> res1 = saveArticle(1L, ArticleSaveRequest.builder()
                                            .userId(1L)
                                            .build());

        ResponseEntity<BaseResponse> res2 = saveArticle4xx(null, ArticleSaveRequest.builder()
                .userId(1L)
                .build());

        ResponseEntity<BaseResponse> res3 = saveArticle4xx(1L, ArticleSaveRequest.builder()
                .userId(null)
                .build());

        assertThat(HttpStatus.OK).isEqualTo(res1.getStatusCode());
        assertThat(HttpStatus.NOT_FOUND).isEqualTo(res2.getStatusCode());
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(res3.getStatusCode());
    }

    ResponseEntity<BaseResponse> saveArticle(Long articleId, ArticleSaveRequest request) {
        return restClient.post()
                .uri("/article/{articleId}/save",articleId)
                .body(request)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> saveArticle4xx(Long articleId, ArticleSaveRequest request) {
        return restClient.post()
                .uri("/article/{articleId}/save",articleId)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static class ArticleSaveRequest {
        private Long userId;
    }

}

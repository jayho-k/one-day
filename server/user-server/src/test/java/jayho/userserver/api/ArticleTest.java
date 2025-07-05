package jayho.userserver.api;


import jayho.userserver.data.DataInitialize;
import jayho.userserver.service.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    DataInitialize dataInitialize;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create(String.format("http://localhost:%s",port));
        dataInitialize.clearArticle();
        dataInitialize.initArticle();
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

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
        Long articleId = 1L;

        ResponseEntity<BaseResponse> res1 = readArticle(articleId);
        ResponseEntity<BaseResponse> res2 = readArticle4xx(null);

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    ResponseEntity<BaseResponse> readArticle(Long articleId) {
        return restClient.get()
                .uri("/article/{articleId}", articleId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> readArticle4xx(Long articleId) {
        return restClient.get()
                .uri("/article/{articleId}", articleId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }


    @Test
    void readAllArticleTest() {
        Integer pageSize = 1;
        Long lastArticleId = 1L;

        ResponseEntity<BaseResponse> res1 = readAllArticle(pageSize, lastArticleId);
        ResponseEntity<BaseResponse> res2 = readAllArticleFirstPage4xx(pageSize, null);
        ResponseEntity<BaseResponse> res3 = readAllArticleFirstPage4xx(null, lastArticleId);

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<BaseResponse> readAllArticle(Integer pageSize, Long lastArticleId) {
        return restClient.get()
                .uri("/article-all?pageSize={pageSize}&lastArticleId={lastArticleId}", pageSize, lastArticleId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> readAllArticleFirstPage(Integer pageSize, Long lastArticleId) {
        return restClient.get()
                .uri("/article-all?pageSize={pageSize}", pageSize)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> readAllArticleFirstPage4xx(Integer pageSize, Long lastArticleId) {
        return restClient.get()
                .uri("/article-all?pageSize={pageSize}", pageSize)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {})
                .toEntity(BaseResponse.class);
    }


    @Test
    void updateArticleTest(){
        ResponseEntity<BaseResponse> res1 = updateArticle(ArticleUpdateRequest.builder()
                .articleId(1L)
                .images(List.of("image1.png"))
                .content("content1")
                .build());

        ResponseEntity<BaseResponse> res2 = updateArticle4xx(ArticleUpdateRequest.builder()
                .articleId(null)
                .images(List.of("image1.png"))
                .content("content123")
                .build());

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<BaseResponse> updateArticle(ArticleUpdateRequest request) {
        return restClient.put()
                .uri("/article")
                .body(request)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> updateArticle4xx(ArticleUpdateRequest request) {
        return restClient.put()
                .uri("/article")
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static class ArticleUpdateRequest{
        private Long articleId;
        private List<String> images;
        private String content;
    }

    @Test
    void tempDeleteArticleTest() {
        ResponseEntity<BaseResponse> res1 = tempDeleteArticle(1L);
        ResponseEntity<BaseResponse> res2 = tempDeleteArticle4xx(null);

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    ResponseEntity<BaseResponse> tempDeleteArticle(Long articleId) {
        return restClient.put()
                .uri("/article/temp-delete/{articleId}", articleId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> tempDeleteArticle4xx(Long articleId) {
        return restClient.put()
                .uri("/article/temp-delete/{articleId}", articleId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Test
    void saveArticleTest() {
        Long articleId = 1L;
        Long userId = 1L;

        ResponseEntity<BaseResponse> res1 = saveArticle(articleId, userId);
        ResponseEntity<BaseResponse> res2 = saveArticle4xx(null, userId);
        ResponseEntity<BaseResponse> res3 = saveArticle4xx(articleId, null);

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    ResponseEntity<BaseResponse> saveArticle(Long articleId, Long userId) {
        return restClient.post()
                .uri("/article/{articleId}/user/{userId}/save",articleId, userId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> saveArticle4xx(Long articleId,Long userId) {
        return restClient.post()
                .uri("/article/{articleId}/user/{userId}/save",articleId, userId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }
}

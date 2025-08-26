package jayho.oneday.api;

import jayho.oneday.service.response.BaseResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestMethodOrder(OrderAnnotation.class)
//@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ArticleLikeTest {

//    @LocalServerPort
//    int port;
    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create(String.format("http://localhost:%s",8080));
//        restClient = RestClient.create(String.format("http://localhost:%s",port));
    }

    @Test
    @Order(1)
    void createLikeTest() {
        Long articleId = 1L;
        Long userId = 1L;

        ResponseEntity<BaseResponse> res1 = createLike(articleId, userId);
        ResponseEntity<BaseResponse> res2 = createLike4xx(null, userId);
        ResponseEntity<BaseResponse> res3 = createLike4xx(articleId, null);

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void likeBulkTest() {
        LongStream.range(25,30).forEach(i -> {
            ResponseEntity<BaseResponse> res = createLike(i, i+2);
            assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        });
    }


    ResponseEntity<BaseResponse> createLike(Long articleId, Long userId) {
        return restClient.post()
                .uri("/article-likes/article/{articleId}/user/{userid}", articleId, userId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> createLike4xx(Long articleId, Long userId) {
        return restClient.post()
                .uri("/article-likes/article/{articleId}", articleId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Order(2)
    @Test
    void deleteLikeTest()   {
        Long articleId = 1L;
        Long userId = 1L;

        ResponseEntity<BaseResponse> res1 = deleteLike(articleId, userId);
        ResponseEntity<BaseResponse> res2 = deleteLike4xx(null, userId);
        ResponseEntity<BaseResponse> res3 = deleteLike4xx(articleId, null);

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    ResponseEntity<BaseResponse> deleteLike(Long articleId, Long userId) {
        return restClient.delete()
                .uri("/article-likes/article/{articleId}/user/{userId}", articleId, userId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }

    ResponseEntity<BaseResponse> deleteLike4xx(Long articleId, Long userId) {
        return restClient.delete()
                .uri("/article-likes/article/{articleId}/user/{userId}", articleId, userId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }


    @Order(3)
    @Test
    void readLikeCountTest() {
        Long articleId = 1L;

        ResponseEntity<BaseResponse> res1 = getLikeCount(articleId);
        ResponseEntity<BaseResponse> res2 = getLikeCount4xx(null);

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    ResponseEntity<BaseResponse> getLikeCount(Long articleId) {
        return restClient.get()
                .uri("/article-likes/article/{articleId}/count", articleId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }

    ResponseEntity<BaseResponse> getLikeCount4xx(Long articleId) {
        return restClient.get()
                .uri("/article-likes/article/{articleId}/count", articleId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }




}

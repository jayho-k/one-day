package jayho.userserver.api;

import jayho.userserver.service.response.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
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
public class ViewTest {

    @LocalServerPort
    int port;
    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create(String.format("http://localhost:%s",port));
    }

    @Test
    void increaseViewCountTest() {
        Long articleId = 1L;
        Long userId = 1L;
        ResponseEntity<BaseResponse> res1 = increaseViewCount(articleId, userId);
        ResponseEntity<BaseResponse> res2 = increaseViewCount4xx(null, userId);
        ResponseEntity<BaseResponse> res3 = increaseViewCount4xx(articleId, null);

        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(res3.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    ResponseEntity<BaseResponse> increaseViewCount(Long articleId, Long userId){
        return restClient.post()
                .uri("/view-count/article/{articleId}/user/{userId}", articleId, userId)
                .retrieve()
                .toEntity(BaseResponse.class);
    }
    ResponseEntity<BaseResponse> increaseViewCount4xx(Long articleId, Long userId){
        return restClient.post()
                .uri("/view-count/article/{articleId}/user/{userId}", articleId, userId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);
    }

    @Test
    void readViewCountTest() {
        Long articleId = 1L;
        ResponseEntity<BaseResponse> res1 = readViewCount(articleId);
        ResponseEntity<BaseResponse> res2 = readViewCount4xx(null);

        System.out.println(res1.getBody());
        assertThat(res1.getStatusCode()).isEqualTo(HttpStatus.OK);

        System.out.println(res2.getBody());
        assertThat(res2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    ResponseEntity<BaseResponse> readViewCount(Long articleId){
        return restClient.get()
                .uri("/view-count/article/{articleId}", articleId)
                .retrieve()
                .toEntity(BaseResponse.class);

    }
    ResponseEntity<BaseResponse> readViewCount4xx(Long articleId){
        return restClient.get()
                .uri("/view-count/article/{articleId}", articleId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toEntity(BaseResponse.class);

    }



}

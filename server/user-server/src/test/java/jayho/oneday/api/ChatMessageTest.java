package jayho.oneday.api;

import jakarta.validation.constraints.NotNull;
import jayho.oneday.service.request.ChatMessageRequest;
import jayho.oneday.service.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ChatMessageTest {

    @LocalServerPort
    int port;
    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create(String.format("http://localhost:%s",8080));
//        restClient = RestClient.create(String.format("http://localhost:%s",port));
    }


    @Test
    void sendMessageTest_success() {

        ChatMessageRequest request = ChatMessageRequest.builder()
                .sessionId("test")
                .message("이번 년도 컬러는 뭐야?")
                .userId(1L)
                .build();

        ResponseEntity<BaseResponse> response = sendMessage(request);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    ResponseEntity<BaseResponse> sendMessage(ChatMessageRequest request) {
        return restClient.post()
                .uri("/chat")
                .body(request)
                .retrieve()
                .toEntity(BaseResponse.class);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    static class ChatMessageRequest {

        @NotNull
        private String sessionId;
        private String message;
        private Long userId;

    }

}

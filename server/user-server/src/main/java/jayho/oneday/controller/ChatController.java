package jayho.oneday.controller;

import jakarta.annotation.PostConstruct;
import jayho.oneday.event.ChatMessageEvent;
import jayho.oneday.service.request.ChatMessageRequest;
import jayho.oneday.service.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private RestClient restClient;
    private final RestTemplate restTemplate;

    // url,port 따로 가져오도록 수정 필요
    private final static String AI_SERVER_URL = "http://localhost:9000";

    @PostMapping("/chat")
    public ResponseEntity<BaseResponse<ChatMessageEvent>> sendMessage(@RequestBody ChatMessageRequest request) {
        ChatMessageEvent chatMessage = ChatMessageEvent.create(
                request.getSessionId(),
                request.getMessage(),
                request.getUserId(),
                "USER"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatMessageEvent> entity = new HttpEntity<>(chatMessage, headers);
        return restTemplate.exchange(
                AI_SERVER_URL+"/ai/chat",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );
    }
}

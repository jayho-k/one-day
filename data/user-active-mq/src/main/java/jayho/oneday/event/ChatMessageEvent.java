package jayho.oneday.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEvent {

    @JsonProperty("session_id")
    private String sessionId;

    private String message;

    @JsonProperty("user_id")
    private Long userId;

    private String target;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    public static ChatMessageEvent create(String sessionId, String message, Long userId, String target ,LocalDateTime createdAt) {
        ChatMessageEvent event = new ChatMessageEvent();
        event.sessionId = sessionId;
        event.message = message;
        event.userId = userId;
        event.target = target;
        event.createdAt = createdAt;
        return event;
    }

    public static ChatMessageEvent create(String sessionId, String message, Long userId, String target) {
        ChatMessageEvent event = new ChatMessageEvent();
        event.sessionId = sessionId;
        event.message = message;
        event.userId = userId;
        event.target = target;
        event.createdAt = LocalDateTime.now();
        return event;
    }

}

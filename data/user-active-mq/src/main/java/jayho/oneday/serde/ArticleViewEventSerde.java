package jayho.oneday.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jayho.oneday.event.ArticleViewEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

@Component
public class ArticleViewEventSerde implements Serde<ArticleViewEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public Serializer<ArticleViewEvent> serializer() {
        return (topic, data) -> {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public Deserializer<ArticleViewEvent> deserializer() {
        return (topic, data) -> {
            try {
                return objectMapper.readValue(data, ArticleViewEvent.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
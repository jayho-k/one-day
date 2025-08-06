package jayho.oneday.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jayho.oneday.event.ArticleViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
@RequiredArgsConstructor
public class ArticleViewEventSerializer implements Serializer<ArticleViewEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public byte[] serialize(String s, ArticleViewEvent test) {
        byte[] serializeTest = null;
        try {
            serializeTest = objectMapper.writeValueAsBytes(test);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return serializeTest;
    }
}

package jayho.oneday.deserializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jayho.oneday.event.ArticleViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ArticleViewEventDeserializer implements Deserializer<ArticleViewEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public ArticleViewEvent deserialize(String topic, byte[] data) {
        ArticleViewEvent deserializerTest = null;
        try {
            deserializerTest = objectMapper.readValue(data, ArticleViewEvent.class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return deserializerTest;
    }

}

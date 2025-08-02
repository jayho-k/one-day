package jayho.oneday.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
@RequiredArgsConstructor
public class TestSerializer implements Serializer<Test> {

    private final ObjectMapper objectMapper;

    @Override
    public byte[] serialize(String s, Test test) {
        byte[] serializeTest = null;
        try {
            serializeTest = objectMapper.writeValueAsBytes(test);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return serializeTest;
    }
}

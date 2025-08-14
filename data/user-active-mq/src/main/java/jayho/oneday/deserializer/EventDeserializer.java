package jayho.oneday.deserializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class EventDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final Class<T> clazz;

    @Override
    public T deserialize(String topic, byte[] data) {
        T deserializer = null;
        try {
            deserializer = objectMapper.readValue(data, clazz);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return deserializer;
    }
}

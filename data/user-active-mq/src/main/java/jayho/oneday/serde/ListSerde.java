package jayho.oneday.serde;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.List;

@RequiredArgsConstructor
public class ListSerde<T> implements Serde<List<T>> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Class<T> clazz;


    @Override
    public Serializer<List<T>> serializer() {
        return (topic, data) -> {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public Deserializer<List<T>> deserializer() {
        return (topic, data) -> {
            try {
                return objectMapper.readValue(
                        data,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}

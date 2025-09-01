package jayho.oneday.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@RequiredArgsConstructor
public class MapSerde<K,V> implements Serde<Map<K,V>> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final Class<K> key;
    private final Class<V> value;

    @Override
    public Serializer<Map<K, V>> serializer() {
        return (topic, data) -> {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public Deserializer<Map<K, V>> deserializer() {
        return (topic, data) -> {
            try {
                return objectMapper.readValue(
                        data,
                        objectMapper.getTypeFactory().constructMapType(Map.class, key, value)
                );
            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        };
    }
}


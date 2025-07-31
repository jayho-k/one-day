package jayho.oneday.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class TestDeserializer implements Deserializer<Test> {

    private final ObjectMapper objectMapper;

    @Override
    public Test deserialize(String topic, byte[] data) {
        Test deserializerTest = null;
        try {
            deserializerTest = objectMapper.readValue(data, Test.class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return deserializerTest;
    }

}

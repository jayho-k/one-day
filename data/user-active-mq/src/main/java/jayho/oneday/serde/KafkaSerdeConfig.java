package jayho.oneday.serde;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import jayho.oneday.ArticleViewEvent;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KafkaSerdeConfig {

    private final String SCHEMA_REGISTRY_URL_CONFIG = "schema.registry.url";

    @Bean("specificAvroValueSerde")
    public SpecificAvroSerde<ArticleViewEvent> specificAvroValueSerde() {
        SpecificAvroSerde<ArticleViewEvent> serde = new SpecificAvroSerde<>();
        serde.configure(Map.of(SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8092"), false);
        return serde;
    }

    @Bean("specificAvroKeySerdeWithRecordKeys")
    public <T extends SpecificRecord> SpecificAvroSerde<T> specificAvroKeySerdeWithRecordKeys() {
        SpecificAvroSerde<T> serde = new SpecificAvroSerde<>();
        serde.configure(Map.of(SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8092"), true);
        return serde;
    }

}

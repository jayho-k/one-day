package jayho.oneday.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import jayho.oneday.ArticleLikeCountEvent;
import jayho.oneday.ArticleLikeEvent;
import jayho.oneday.ArticleViewEvent;
import jayho.oneday.event.ChatMessageEvent;
import jayho.oneday.serializer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    @Qualifier("producerDefaultFactory")
    public ProducerFactory<String, String> producerDefaultFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8092");
        props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true); // 스키마 자동 등록
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @Qualifier("kafkaStringTemplate")
    public KafkaTemplate<String, String> kafkaStringTemplate() {
        return new KafkaTemplate<>(producerDefaultFactory());
    }

    @Bean
    @Qualifier("producerArticleViewFactory")
    public ProducerFactory<String, ArticleViewEvent> producerArticleViewFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8092");
        props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true); // 스키마 자동 등록
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @Qualifier("kafkaArticleViewTemplate")
    public KafkaTemplate<String, ArticleViewEvent> kafkaArticleViewTemplate() {
        return new KafkaTemplate<>(producerArticleViewFactory());
    }

    @Bean
    @Qualifier("producerArticleLikeFactory")
    public ProducerFactory<String, ArticleLikeEvent> producerArticleLikeFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8092");
        props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true); // 스키마 자동 등록
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @Qualifier("kafkaArticleLikeTemplate")
    public KafkaTemplate<String, ArticleLikeEvent> kafkaArticleLikeTemplate() {
        return new KafkaTemplate<>(producerArticleLikeFactory());
    }

    @Bean
    @Qualifier("producerArticleLikeCountFactory")
    public ProducerFactory<String, ArticleLikeCountEvent> producerArticleLikeCountFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8092");
        props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true); // 스키마 자동 등록
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @Qualifier("kafkaArticleLikeCountTemplate")
    public KafkaTemplate<String, ArticleLikeCountEvent> kafkaArticleLikeCountTemplate() {
        return new KafkaTemplate<>(producerArticleLikeCountFactory());
    }

    @Bean
    @Qualifier("kafkaChatMessageFactory")
    public ProducerFactory<String, ChatMessageEvent> producerChatMessageFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ChatMessageSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @Qualifier("kafkaChatMessageTemplate")
    public KafkaTemplate<String, ChatMessageEvent> kafkaChatMessageTemplate() {
        return new KafkaTemplate<>(producerChatMessageFactory());
    }
}

package jayho.oneday.config;

import jayho.oneday.event.ArticleLikeCountEvent;
import jayho.oneday.event.ArticleLikeEvent;
import jayho.oneday.event.ArticleViewEvent;
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
        EventSerializer<ArticleViewEvent> serializer = new EventSerializer<>();
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), serializer);
    }

    @Bean
    @Qualifier("kafkaArticleViewTemplate")
    public KafkaTemplate<String, ArticleViewEvent> kafkaArticleViewTemplate() {
        return new KafkaTemplate<>(producerArticleViewFactory());
    }

    @Bean
    @Qualifier("producerArticleLikeFactory")
    public ProducerFactory<String, ArticleLikeEvent> producerArticleLikeFactory() {
        EventSerializer<ArticleLikeEvent> serializer = new EventSerializer<>();
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), serializer);
    }

    @Bean
    @Qualifier("kafkaArticleLikeTemplate")
    public KafkaTemplate<String, ArticleLikeEvent> kafkaArticleLikeTemplate() {
        return new KafkaTemplate<>(producerArticleLikeFactory());
    }

    @Bean
    @Qualifier("producerArticleLikeCountFactory")
    public ProducerFactory<String, ArticleLikeCountEvent> producerArticleLikeCountFactory() {
        EventSerializer<ArticleLikeCountEvent> serializer = new EventSerializer<>();
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), serializer);
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

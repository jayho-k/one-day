package jayho.oneday.config;

import jayho.oneday.deserializer.ArticleLikeCountEventDeserializer;
import jayho.oneday.deserializer.ArticleLikeEventDeserializer;
import jayho.oneday.deserializer.ChatMessageDeserializer;
import jayho.oneday.event.ArticleLikeCountEvent;
import jayho.oneday.event.ArticleLikeEvent;
import jayho.oneday.event.ArticleViewEvent;
import jayho.oneday.deserializer.ArticleViewEventDeserializer;
import jayho.oneday.event.ChatMessageEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;


import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    @Qualifier("consumerArticleViewFactory")
    public ConsumerFactory<String, ArticleViewEvent> consumerArticleViewFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "article-view-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ArticleViewEventDeserializer.class);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    @Qualifier("articleViewKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ArticleViewEvent> articleViewKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ArticleViewEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerArticleViewFactory());
        return factory;
    }

    @Bean
    @Qualifier("consumerArticleLikeFactory")
    public ConsumerFactory<String, ArticleLikeEvent> consumerArticleLikeFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "article-like-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ArticleLikeEventDeserializer.class);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    @Qualifier("articleLikeKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ArticleLikeEvent> articleLikeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ArticleLikeEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerArticleLikeFactory());
        factory.setBatchListener(true);
        factory.setCommonErrorHandler(new DefaultErrorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        return factory;
    }

    @Bean
    @Qualifier("consumerArticleLikeCountFactory")
    public ConsumerFactory<String, ArticleLikeCountEvent> consumerArticleLikeCountFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "article-like-count-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ArticleLikeCountEventDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    @Qualifier("articleLikeCountKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ArticleLikeCountEvent> articleLikeCountKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ArticleLikeCountEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerArticleLikeCountFactory());
        return factory;
    }

    @Bean
    @Qualifier("consumerChatMessageFactory")
    public ConsumerFactory<String, ChatMessageEvent> consumerChatMessageFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "chat-ai-message-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ChatMessageDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    @Qualifier("chatMessageKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ChatMessageEvent> chatMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatMessageEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerChatMessageFactory());
        return factory;
    }

}

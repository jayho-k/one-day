package jayho.oneday.config;

import jayho.oneday.deserializer.ArticleLikeEventDeserializer;
import jayho.oneday.event.ArticleLikeEvent;
import jayho.oneday.event.ArticleViewEvent;
import jayho.oneday.deserializer.ArticleViewEventDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;


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
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "default-group");
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
        return factory;
    }

}

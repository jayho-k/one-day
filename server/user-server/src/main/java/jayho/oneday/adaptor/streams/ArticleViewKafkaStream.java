package jayho.oneday.adaptor.streams;

import jayho.oneday.entity.ArticleViewCount;
import jayho.oneday.event.ArticleViewEvent;
import jayho.oneday.serializer.ArticleViewEventSerde;
import jayho.oneday.serializer.ListSerde;
import jayho.oneday.serializer.MapSerde;
import jayho.oneday.service.ArticleViewService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;


import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class ArticleViewKafkaStream {

    private static final String TOPIC_ARTICLE_VIEW = "topic-article-view";
    private final ArticleViewService articleViewService;
    private final ArticleViewEventSerde articleViewEventSerde;

    @Bean
    public KStream<String, ArticleViewEvent> countArticleViewStream(StreamsBuilder builder) {

        KStream<String, ArticleViewEvent> stream = builder.stream(
                TOPIC_ARTICLE_VIEW, Consumed.with(Serdes.String(), articleViewEventSerde)
        );

        stream.groupByKey()
                .windowedBy(TimeWindows.ofSizeAndGrace(
                        Duration.ofSeconds(10),
                        Duration.ofSeconds(10)))
                .aggregate(
                        HashMap::new,
                        (String key, ArticleViewEvent articleView, Map<Long, Long> articleViewMap) -> {
                            articleViewMap.merge(articleView.getArticleId(), 1L, Long::sum);
                            return articleViewMap;
                        },
                        Materialized.with(
                                Serdes.String(),
                                new MapSerde<>(Long.class, Long.class)
                        ))
                .suppress(Suppressed.untilWindowCloses(
                        Suppressed.BufferConfig.unbounded()))// 임시
                .toStream()
                .foreach((windowedKey, articleViewMap) ->{
                        System.out.println("articleViewMap" + articleViewMap.toString());
                        articleViewService.increaseViewCountBackup(
                            articleViewMap.entrySet().stream().map(
                                    event -> ArticleViewCount.create(event.getKey(), event.getValue())
                            ).toList());
                    }
                );
        return stream;
    }
}

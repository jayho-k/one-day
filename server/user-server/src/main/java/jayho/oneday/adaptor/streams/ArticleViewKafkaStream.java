package jayho.oneday.adaptor.streams;

import jayho.oneday.entity.ArticleViewCount;
import jayho.oneday.event.ArticleViewEvent;
import jayho.oneday.serde.ArticleViewEventSerde;
import jayho.oneday.serde.MapSerde;
import jayho.oneday.service.ArticleViewService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;


import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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

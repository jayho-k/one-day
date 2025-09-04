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
import java.util.List;
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

        // TODO: Test
         stream
            .groupByKey()
            .windowedBy(TimeWindows.ofSizeAndGrace(
                    Duration.ofSeconds(10),
                    Duration.ofSeconds(10)
            ))
            .count(Materialized.with(Serdes.String(), Serdes.Long())) // Key는 String
            .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
            .toStream()
            .foreach((windowedKey, count) -> {

                System.out.println("windowed key: " + windowedKey.key());
                System.out.println("count: " + count);
                articleViewService.increaseViewCountBackup(
                        List.of(ArticleViewCount.create(Long.valueOf(windowedKey.key()), count))
                );
            });
        return stream;
    }
}

package jayho.oneday.adaptor.producer;

import jayho.oneday.ArticleViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleViewProducer {

    private static final String TOPIC_ARTICLE_VIEW = "topic-article-view";

    @Qualifier("kafkaArticleViewTemplate")
    private final KafkaTemplate<String, ArticleViewEvent> kafkaArticleViewTemplate;

    public void increaseViewCount(Long articleId, Long userId) {
        ArticleViewEvent articleViewCount = ArticleViewEvent.newBuilder()
                .setArticleId(articleId)
                .setUserId(userId)
                .build();

        // partition 1개로 임시지정 >> partition 전략 수립 후 key값 정리 필요
        ProducerRecord<String, ArticleViewEvent> producerRecord = new ProducerRecord<>(TOPIC_ARTICLE_VIEW, articleId.toString(), articleViewCount);
        kafkaArticleViewTemplate.send(producerRecord)
                .thenAccept(result ->
                        log.info("[increaseViewCount] send success{}", result))
                .exceptionally(ex -> {
                    log.error("[increaseViewCount] send failed{}", ex.getMessage());
                    return null;
                });
    }

}

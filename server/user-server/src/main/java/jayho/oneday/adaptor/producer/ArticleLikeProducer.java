package jayho.oneday.adaptor.producer;

import jayho.oneday.event.ArticleLikeEvent;
import jayho.oneday.event.ArticleViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleLikeProducer {

    private static final String TOPIC_ARTICLE_LIKE = "topic-article-like";

    @Qualifier("kafkaArticleLikeTemplate")
    private final KafkaTemplate<String, ArticleLikeEvent> kafkaArticleLikeTemplate;

    public void articleLikeMessage(Long articleId, Long userId, Boolean isLiked) {
        ArticleLikeEvent articleLikeEvent = ArticleLikeEvent.create(articleId, userId, isLiked);

        // partition 1개로 임시지정 >> partition 전략 수립 후 key값 정리 필요
        ProducerRecord<String, ArticleLikeEvent> articleLikeEventProducerRecord = new ProducerRecord<>(TOPIC_ARTICLE_LIKE, TOPIC_ARTICLE_LIKE, articleLikeEvent);
        kafkaArticleLikeTemplate.send(articleLikeEventProducerRecord)
                .thenAccept(result ->
                        log.info("[increaseViewCount] send success{}", result))
                .exceptionally(ex -> {
                    log.error("[increaseViewCount] send failed{}", ex.getMessage());
                    return null;
                });
    }
}

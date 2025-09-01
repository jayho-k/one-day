package jayho.oneday.adaptor.producer;

import jayho.oneday.entity.ArticleLikeCount;
import jayho.oneday.event.ArticleLikeCountEvent;
import jayho.oneday.event.ArticleLikeEvent;
import jayho.oneday.event.ArticleViewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleLikeProducer {

    private static final String TOPIC_ARTICLE_LIKE = "topic-article-like";
    private static final String TOPIC_ARTICLE_LIKE_COUNT = "topic-article-like-count";


    @Qualifier("kafkaArticleLikeTemplate")
    private final KafkaTemplate<String, ArticleLikeEvent> kafkaArticleLikeTemplate;

    @Qualifier("kafkaArticleLikeCountTemplate")
    private final KafkaTemplate<String, ArticleLikeCountEvent> kafkaArticleLikeCountTemplate;


    public void articleLikeMessage(Long articleId, Long userId, Boolean isLiked) {
        ArticleLikeEvent articleLikeEvent = ArticleLikeEvent.create(articleId, userId, isLiked);

        // partition key : articleId
        ProducerRecord<String, ArticleLikeEvent> articleLikeEventProducerRecord = new ProducerRecord<>(TOPIC_ARTICLE_LIKE, articleId.toString(), articleLikeEvent);
        kafkaArticleLikeTemplate.send(articleLikeEventProducerRecord)
                .thenAccept(result ->
                        log.info("[increaseViewCount] send success{}", result))
                .exceptionally(ex -> {
                    log.error("[increaseViewCount] send failed{}", ex.getMessage());
                    return null;
                });
    }

    // bundle
    public void articleLikeCountMessage(Map<Long, ArticleLikeCountEvent> articleCountEventMap) {
        articleCountEventMap.forEach((articleId, articleLikeCountEvent) -> {
            // partition 1개로 임시지정 >> partition 전략 수립 후 key값 정리 필요
            ProducerRecord<String, ArticleLikeCountEvent> articleLikeCountEventProducerRecord = new ProducerRecord<>(TOPIC_ARTICLE_LIKE_COUNT, TOPIC_ARTICLE_LIKE_COUNT, articleLikeCountEvent);
            kafkaArticleLikeCountTemplate.send(articleLikeCountEventProducerRecord)
                    .thenAccept(result ->
                            log.info("[increaseViewCount] send success{}", result))
                    .exceptionally(ex -> {
                        log.error("[increaseViewCount] send failed{}", ex.getMessage());
                        return null;
                    });
        });
    }

    // single
    public void articleLikeCountMessage(Long articleId, Long count, Boolean isLiked) {
        ArticleLikeCountEvent articleLikeCountEvent = ArticleLikeCountEvent.create(articleId, count, isLiked);

        // partition 1개로 임시지정 >> partition 전략 수립 후 key값 정리 필요
        ProducerRecord<String, ArticleLikeCountEvent> articleLikeCountEventProducerRecord = new ProducerRecord<>(TOPIC_ARTICLE_LIKE_COUNT, TOPIC_ARTICLE_LIKE_COUNT, articleLikeCountEvent);
        kafkaArticleLikeCountTemplate.send(articleLikeCountEventProducerRecord)
                .thenAccept(result ->
                        log.info("[increaseViewCount] send success{}", result))
                .exceptionally(ex -> {
                    log.error("[increaseViewCount] send failed{}", ex.getMessage());
                    return null;
                });
    }



}

package jayho.oneday.adaptor.consumer;

import jayho.oneday.entity.Article;
import jayho.oneday.entity.ArticleLike;
import jayho.oneday.event.ArticleLikeCountEvent;
import jayho.oneday.event.ArticleLikeEvent;
import jayho.oneday.service.ArticleLikeService;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ArticleLikeConsumer {

    private static final String TOPIC_ARTICLE_LIKE = "topic-article-like";
    private static final String TOPIC_ARTICLE_LIKE_COUNT = "topic-article-like-count";
    private final ArticleLikeService articleLikeService;

    @KafkaListener(
            topics = TOPIC_ARTICLE_LIKE,
            groupId = "article-like-group",
            containerFactory = "articleLikeKafkaListenerContainerFactory",
            properties = {
                    ConsumerConfig.FETCH_MIN_BYTES_CONFIG + ":5242880", // 5MB
                    ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG + ":5000" // 5초
            }
    )
    public void listenArticleLike(List<ArticleLikeEvent> events) {
        articleLikeService.validateArticleLikeBulk(events.stream().map(event ->
                ArticleLike.create(
                        event.getArticleId(),
                        event.getUserId(),
                        event.getLike()
                )
        ).toList());
    }

    @KafkaListener(
            topics = TOPIC_ARTICLE_LIKE_COUNT,
            groupId = "article-like-count-group",
            containerFactory = "articleLikeCountKafkaListenerContainerFactory"
    )
    public void listenArticleLikeCount(ArticleLikeCountEvent articleLikeCountEvent) {
            articleLikeService.articleLikeCountingMQ(
                    articleLikeCountEvent.getArticleId(),
                    articleLikeCountEvent.getLike(),
                    articleLikeCountEvent.getCount());
    }
}
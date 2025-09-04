package jayho.oneday.adaptor.consumer;

import jayho.oneday.event.ArticleLikeCountEvent;
import jayho.oneday.event.ArticleViewEvent;
import jayho.oneday.service.HotArticleService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotArticleConsumer {

    private static final String TOPIC_ARTICLE_LIKE_COUNT = "topic-article-like-count";
    private static final String TOPIC_ARTICLE_VIEW = "topic-article-view";

    private final HotArticleService hotArticleService;


    @KafkaListener(
            topics = TOPIC_ARTICLE_LIKE_COUNT,
            groupId = "hot-article-group",
            containerFactory = "articleLikeCountKafkaListenerContainerFactory"
    )
    public void listenArticleLikeCount(ArticleLikeCountEvent articleLikeCountEvent) {
        // Long articleId, String topicName, Long score
        hotArticleService.updateArticleScore(
                articleLikeCountEvent.getArticleId(),
                TOPIC_ARTICLE_LIKE_COUNT,
                articleLikeCountEvent.getLike()
                        ? articleLikeCountEvent.getCount()
                        : -articleLikeCountEvent.getCount()
        );
    }

    @KafkaListener(
            topics = TOPIC_ARTICLE_VIEW,
            groupId = "hot-article-group",
            containerFactory = "articleViewKafkaListenerContainerFactory"
    )
    public void listenIncreaseArticleView(ArticleViewEvent articleViewEvent) {
        hotArticleService.updateArticleScore(
                articleViewEvent.getArticleId(),
                TOPIC_ARTICLE_VIEW,
                1L
        );
    }


}


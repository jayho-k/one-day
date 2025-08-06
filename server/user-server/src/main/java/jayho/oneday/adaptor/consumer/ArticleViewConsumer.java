package jayho.oneday.adaptor.consumer;

import jayho.oneday.event.ArticleViewEvent;
import jayho.oneday.service.ArticleViewService;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleViewConsumer {

    private static final String TOPIC_ARTICLE_VIEW = "topic-article-view";

    private final ArticleViewService articleViewService;

    @KafkaListener(
            topics = TOPIC_ARTICLE_VIEW,
            groupId = "increase-view-count-cache",
            containerFactory = "articleViewKafkaListenerContainerFactory"
    )
    public void listenIncreaseArticleView(ArticleViewEvent articleViewEvent) {
        articleViewService.increaseViewCount(articleViewEvent);
//        ack.acknowledge();
    }


}

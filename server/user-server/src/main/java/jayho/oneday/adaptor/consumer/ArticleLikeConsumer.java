package jayho.oneday.adaptor.consumer;

import jayho.oneday.event.ArticleLikeEvent;
import jayho.oneday.event.ArticleViewEvent;
import jayho.oneday.service.ArticleLikeService;
import jayho.oneday.service.ArticleViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleLikeConsumer {

    private static final String TOPIC_ARTICLE_LIKE = "topic-article-like";

    private final ArticleLikeService articleLikeService;

    @KafkaListener(
            topics = TOPIC_ARTICLE_LIKE,
            groupId = "article-like",
            containerFactory = "articleLikeKafkaListenerContainerFactory"
    )
    public void listenArticleLike(ArticleLikeEvent articleLikeEvent) {
        try {
            articleLikeService.articleLikeCountingMQ(articleLikeEvent.getArticleId(), articleLikeEvent.getLike());
        }catch (NegativeArraySizeException e){
            // log or db 저장 필요할 수도 : 데이터 역전이 된다면 마이너스 값이 나올 수도 있음
            e.printStackTrace();
        }

    }

}

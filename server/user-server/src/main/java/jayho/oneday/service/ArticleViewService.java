package jayho.oneday.service;

import jayho.oneday.adaptor.producer.ArticleViewProducer;
import jayho.oneday.controller.exception.AbusingException;
import jayho.oneday.entity.ArticleViewCount;
import jayho.oneday.event.ArticleViewEvent;
import jayho.oneday.repository.ArticleViewAbusingMemoryRepository;
import jayho.oneday.repository.ArticleViewCountRepository;
import jayho.oneday.repository.ViewMemoryRepository;
import jayho.oneday.service.response.ArticleViewResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MINUTES;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleViewService {

    // db에서 관리 할지 고민
    private static final Long TTL = 5L;
    private static final TimeUnit TTL_TIME_UNIT = MINUTES;

    // Caching Service
    private final ArticleViewCache articleViewCache;

    // redis
    private final ViewMemoryRepository viewMemoryRepository;
    private final ArticleViewAbusingMemoryRepository viewAbusingMemoryRepository;

    // rdb
    private final ArticleViewCountRepository articleViewCountRepository;

    // mq
    private final ArticleViewProducer articleViewProducer;

    public void increaseViewCountToMQ(Long articleId, Long userId) {
        if (viewAbusingMemoryRepository.isAbused(articleId, userId)) {
            throw new AbusingException(String.format("현재 유저는 %s 분이 넘지 않았습니다.", TTL));
        }
        viewAbusingMemoryRepository.register(articleId, userId, TTL, TTL_TIME_UNIT);
        articleViewProducer.increaseViewCount(articleId, userId);
    }


    public void increaseViewCountBackup(List<ArticleViewCount> articleViewCountList) {
        articleViewCountRepository.updateAll(articleViewCountList);
    }

    public Long increaseViewCount(ArticleViewEvent articleView) {
        return viewMemoryRepository.increase(articleView.getArticleId());
    }

    public ArticleViewCount readViewCount(Long articleId) {
        return ArticleViewCount.create(articleId, articleViewCache.readViewCount(articleId));
    }



//    public ArticleViewResponseData increaseViewCountMQTest(Long articleId, Long userId) {
//        articleViewProducer.increaseViewCount(articleId, userId);
//        return increaseViewCount(articleId, userId);
//    }

//    public ArticleViewResponseData increaseViewCount(Long articleId, Long userId) {
//        // 동일 User가 ttl 동안 해당 article view count x
//        Long viewCount = articleViewCache.readViewCount(articleId);
//
//        // increase x
//        if (viewAbusingMemoryRepository.isAbused(articleId, userId)) {
//            return ArticleViewResponseData.from(ArticleViewCount.create(articleId, viewCount));
//        }
//
//        // abusing register + increase
//        viewAbusingMemoryRepository.register(articleId, userId, TTL, TTL_TIME_UNIT);
//        ArticleViewCount articleViewCount = ArticleViewCount.create(articleId, viewMemoryRepository.increase(articleId));
//        if (viewCount == 0L) {
//            articleViewCountRepository.save(articleViewCount);
//        }
//        return ArticleViewResponseData.from(articleViewCount);
//    }
}

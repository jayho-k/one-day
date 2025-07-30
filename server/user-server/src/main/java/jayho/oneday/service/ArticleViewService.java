package jayho.oneday.service;

import jayho.oneday.entity.ArticleViewCount;
import jayho.oneday.repository.ArticleViewAbusingMemoryRepository;
import jayho.oneday.repository.ArticleViewCountRepository;
import jayho.oneday.repository.ViewMemoryRepository;
import jayho.oneday.service.response.ArticleViewResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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


    public ArticleViewResponseData increaseViewCount(Long articleId, Long userId) {
        // 동일 User가 ttl 동안 해당 article view count x
        Long viewCount = articleViewCache.readViewCount(articleId);

        // increase x
        if (viewAbusingMemoryRepository.isAbused(articleId, userId)) {
            return ArticleViewResponseData.from(ArticleViewCount.create(articleId, viewCount));
        }

        // abusing register + increase
        viewAbusingMemoryRepository.register(articleId, userId, ttl, ttlTimeUnit);
        ArticleViewCount articleViewCount = ArticleViewCount.create(articleId, viewMemoryRepository.increase(articleId));
        if (viewCount == 0L) {
            articleViewCountRepository.save(articleViewCount);
        }
        return ArticleViewResponseData.from(articleViewCount);
    }

    public ArticleViewCount readViewCount(Long articleId) {
        return ArticleViewCount.create(articleId, articleViewCache.readViewCount(articleId));
    }

    @Scheduled(fixedDelay = 7, timeUnit = TimeUnit.DAYS)
    public void backUpArticleViewCount() {
        // pub/sub or MQ 고려
    }

}

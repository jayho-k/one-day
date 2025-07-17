package jayho.oneday.service;

import jayho.oneday.entity.ArticleViewCount;
import jayho.oneday.repository.ArticleViewAbusingMemoryRepository;
import jayho.oneday.repository.ArticleViewCountRepository;
import jayho.oneday.repository.ViewMemoryRepository;
import jayho.oneday.service.response.ArticleViewResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
@RequiredArgsConstructor
public class ViewService {

    // db에서 관리 할지 고민
    private static final Long ttl = 5L;
    private static final TimeUnit ttlTimeUnit = MINUTES;

    private final ViewMemoryRepository viewMemoryRepository; // redis
    private final ArticleViewAbusingMemoryRepository viewAbusingMemoryRepository; // redis
    private final ArticleViewCountRepository articleViewCountRepository; // rdb


    public ArticleViewResponseData increaseViewCount(Long articleId, Long userId) {
        // 동일 User가 ttl 동안 해당 article view count x
        Long articleView = viewMemoryRepository.read(articleId);
        // increase
        if (viewAbusingMemoryRepository.read(articleId, userId) == null) {
            viewAbusingMemoryRepository.save(articleId, userId, ttl, ttlTimeUnit);
            Long articleViewIncreased = viewMemoryRepository.increase(articleId, Instant.now().toEpochMilli());
            // init -> rdb
            if (articleView == 0L) {
                articleViewCountRepository.save(
                        ArticleViewCount.create(articleId, articleViewIncreased)
                );
            }
            return ArticleViewResponseData.from(ArticleViewCount.create(articleId, articleView));
        }
        // increase x
        if (articleView == 0L) {
            viewMemoryRepository.setKey(
                    articleId,
                    readViewCount(articleId).getArticleViewCount());
        }
        return ArticleViewResponseData.from(ArticleViewCount.create(articleId, articleView));
    }

    // @Cacheable >> 위에 로직 변경
    public ArticleViewResponseData readViewCount(Long articleId) {
        return ArticleViewResponseData.from(
                articleViewCountRepository.findById(articleId).orElseThrow()
        );
    }

    @Scheduled(fixedDelay = 7, timeUnit = TimeUnit.DAYS)
    public void backUpArticleViewCount() {
        // rdb에서 뽑아와서 해당 key값들의 기간을 확인한 후 삭제 시도
        // epoch로 계산 필요
    }

}

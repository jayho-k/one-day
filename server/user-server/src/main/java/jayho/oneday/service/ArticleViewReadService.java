package jayho.oneday.service;

import jayho.oneday.config.CacheableKeyFormatMapping;
import jayho.oneday.entity.ArticleViewCount;
import jayho.oneday.repository.ArticleViewCountRepository;
import jayho.oneday.repository.ViewMemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleViewReadService {

    private final ArticleViewCountRepository articleViewCountRepository;

    @Cacheable(value = ViewMemoryRepository.CACHE_NAME, keyGenerator = "cacheableKeyFormatGenerator")
    @CacheableKeyFormatMapping(keyFormat = ViewMemoryRepository.KEY_FORMAT, keys = {"#articleId"}) // 삭제 후 변경
    public Long readViewCount(Long articleId) {
        log.info("readViewCount cache miss. articleId:{}", articleId);
        return articleViewCountRepository.findById(articleId)
                .map(ArticleViewCount::getArticleView)
                .orElse(0L);
    }
}

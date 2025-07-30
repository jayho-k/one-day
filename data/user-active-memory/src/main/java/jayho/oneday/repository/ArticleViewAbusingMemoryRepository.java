package jayho.oneday.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ArticleViewAbusingMemoryRepository {

    private final StringRedisTemplate stringRedisTemplate;

    public static final String CACHE_NAME = "article::view";

    // article::view::articleId::{articleId}::userId::{userId}
    private static final String KEY_FORMAT = "articleId::%s::userId::%s";

    public Boolean register(Long articleId, Long userId, Long timeout, TimeUnit unit) {
        return stringRedisTemplate.opsForValue().setIfAbsent(generateKey(articleId, userId),"",timeout, unit);
    }

    public Boolean isAbused(Long articleId, Long userId) {
        return stringRedisTemplate.hasKey(generateKey(articleId, userId));
    }

    private String generateKey(Long articleId, Long userId) {
        return CACHE_NAME+"::"+KEY_FORMAT.formatted(articleId,userId);
    }

}

package jayho.oneday.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleLikeMemoryRepository {

    private final StringRedisTemplate stringRedisTemplate;

    public static final String CACHE_NAME = "article::like";

    // article::like::count::{articleId}
    public static final String KEY_FORMAT = "count::articleId::%s";

    public Long increase(Long articleId) {
        return stringRedisTemplate.opsForValue().increment(generateKey(articleId));
    }

    public Long decrease(Long articleId) {
        return stringRedisTemplate.opsForValue().decrement(generateKey(articleId));
    }

    public void setLikeCount(Long articleId, Long count) {
        stringRedisTemplate.opsForValue().set(generateKey(articleId), String.valueOf(count));
    }

    public Long read(Long articleId) {
        String value = stringRedisTemplate.opsForValue().get(generateKey(articleId));
        if (value == null) {
            return null;
        }
        return Long.valueOf(value);
    }

    private String generateKey(Long articleId) {
        return CACHE_NAME+"::"+KEY_FORMAT.formatted(articleId);
    }

}

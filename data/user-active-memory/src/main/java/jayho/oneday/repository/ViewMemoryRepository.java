package jayho.oneday.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ViewMemoryRepository {

    private final StringRedisTemplate stringRedisTemplate;

    public static final String CACHE_NAME = "article::view";

    // article::view::count::{articleId}
    public static final String KEY_FORMAT = "count::articleId::%s";

    public Long increase(Long articleId) {
        return stringRedisTemplate.opsForValue().increment(generateKey(articleId));
    }

    public Long read(Long articleId) {
        String value = stringRedisTemplate.opsForValue().get(generateKey(articleId));
        if (value == null) {
            return 0L;
        }
        return Long.valueOf(value);
    }

    public void setKey(Long articleId, Long articleView) {
        stringRedisTemplate.opsForValue().set(generateKey(articleId), articleView.toString());
    }

    private String generateKey(Long articleId) {
        return CACHE_NAME+"::"+KEY_FORMAT.formatted(articleId);
    }
}

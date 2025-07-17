package jayho.oneday.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ViewMemoryRepository {

    private final StringRedisTemplate stringRedisTemplate;

    // view:count:{articleId}
    private static final String KEY = "article:view:count:%s";

    public Long increase(Long articleId, Long epochTime) {
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
        return KEY.formatted(articleId);
    }


}

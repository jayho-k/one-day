package jayho.oneday.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ArticleViewAbusingMemoryRepository {

    private final StringRedisTemplate stringRedisTemplate;

    // article:view:articleId:userId
    private static final String KEY = "article:view:%s:%s";

    public Boolean save(Long articleId, Long userId, Long timeout, TimeUnit unit) {
        return stringRedisTemplate.opsForValue().setIfAbsent(generateKey(articleId, userId),"",timeout, unit);
    }

    public String read(Long articleId, Long userId) {
        return stringRedisTemplate.opsForValue().get(generateKey(articleId, userId));
    }

    private String generateKey(Long articleId, Long userId) {
        return String.format(KEY, articleId, userId);
    }


}

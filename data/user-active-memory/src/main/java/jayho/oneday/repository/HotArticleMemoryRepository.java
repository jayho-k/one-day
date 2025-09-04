package jayho.oneday.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class HotArticleMemoryRepository {

    private final StringRedisTemplate stringRedisTemplate;

    public static final String CACHE_NAME = "article::hot-article";

    public static final String KEY_FORMAT = "articleId::%s";

    public Double updateScore(Long articleId, Long score) {
        return stringRedisTemplate.opsForZSet().incrementScore(CACHE_NAME, generateKey(articleId), score);
    }

    public List<Long> getHotArticle(int top) {
        return Objects.requireNonNull(stringRedisTemplate.opsForZSet().reverseRange(CACHE_NAME, 0, top - 1)).stream()
                .map(k -> Long.parseLong(k.replace("articleId::", "")))
                .toList();
    }

    private String generateKey(Long articleId) {
        return CACHE_NAME+"::"+KEY_FORMAT.formatted(articleId);
    }


}

package jayho.oneday.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues();
        return RedisCacheManager.builder(factory)
                .cacheDefaults(cacheConfig)
                .enableCreateOnMissingCache()
                .build();
    }

    @Bean
    public ExpressionParser cacheParser() {
        return new SpelExpressionParser();
    }

    @Bean
    public StandardEvaluationContext cacheContext() {
        return new StandardEvaluationContext();
    }

}

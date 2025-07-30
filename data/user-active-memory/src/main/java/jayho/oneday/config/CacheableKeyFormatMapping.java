package jayho.oneday.config;

import org.springframework.aot.hint.annotation.Reflective;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Reflective
public @interface CacheableKeyFormatMapping {
    String keyFormat();
    String[] keys();
}

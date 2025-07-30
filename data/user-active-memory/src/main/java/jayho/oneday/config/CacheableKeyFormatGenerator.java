package jayho.oneday.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@Component("cacheableKeyFormatGenerator")
@RequiredArgsConstructor
public class CacheableKeyFormatGenerator implements KeyGenerator {

    private final ExpressionParser cacheParser;
    private final StandardEvaluationContext cacheContext;

    @Override
    public Object generate(Object target, Method method, Object... params) {

        if (!method.isAnnotationPresent(CacheableKeyFormatMapping.class)){
            throw new RuntimeException("KeyFormat is not present");
        }
        setParameters(method.getParameters(), params);
        CacheableKeyFormatMapping keyFormatAnnotation = method.getAnnotation(CacheableKeyFormatMapping.class);
        String keyFormat = keyFormatAnnotation.keyFormat();
        String formatted = keyFormat.formatted(
                Arrays.stream(keyFormatAnnotation.keys())
                        .map(key -> cacheParser.parseExpression(key).getValue(cacheContext)).toArray());
        return formatted;
    }

    private void setParameters(Parameter[] parameters, Object... params) {
        for (int i = 0; i < parameters.length; i++) {
            cacheContext.setVariable(parameters[i].getName(), params[i]);
        }
    }

}

package jayho.oneday.config;

import jayho.common.snowflake.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdGeneratorConfig {

    @Bean
    public Snowflake snowflake() {
        return new Snowflake();
    }

}

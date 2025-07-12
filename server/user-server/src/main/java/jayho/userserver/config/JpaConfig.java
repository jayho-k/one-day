package jayho.userserver.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "jayho.useractiverdb")
@EnableJpaRepositories(basePackages = "jayho.useractiverdb")
public class JpaConfig {
}

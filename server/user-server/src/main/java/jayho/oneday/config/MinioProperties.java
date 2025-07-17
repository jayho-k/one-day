package jayho.oneday.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.minio")
public class MinioProperties {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucketName;

}

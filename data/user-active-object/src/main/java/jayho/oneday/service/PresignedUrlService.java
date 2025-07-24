package jayho.oneday.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import jayho.oneday.config.MinioProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PresignedUrlService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;


    public String getUploadPresignedUrl(String imageName) {
        String presignedtUrl = "";
        try {
            presignedtUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(minioProperties.getBucketName())
                            .object(imageName)
                            .expiry(10, TimeUnit.MINUTES)
                            .build());
        } catch (IOException | ServerException | InternalException | XmlParserException | InvalidResponseException |
                 InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InsufficientDataException e) {
            throw new RuntimeException(e);
        }
        return presignedtUrl;
    }

    public String getDownloadPresignedUrl(String imageName) {
        String presignedtUrl = "";
        Map<String, String> reqParams  = Map.of("response-content-type", "application/json");
        try {
            presignedtUrl =
                    minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .bucket(minioProperties.getBucketName())
                                    .object(imageName)
                                    .expiry(10, TimeUnit.MINUTES)
                                    .extraQueryParams(reqParams)
                                    .build());
        } catch (IOException | ServerException | InternalException | XmlParserException | InvalidResponseException |
                 InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InsufficientDataException e) {
            throw new RuntimeException(e);
        }
        return presignedtUrl;
    }



}

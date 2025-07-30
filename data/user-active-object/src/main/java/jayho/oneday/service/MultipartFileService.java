package jayho.oneday.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jayho.oneday.config.MinioProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MultipartFileService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public void uploadImageMultipartFile(String imageName, InputStream inputStream, Long size, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(imageName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (IOException | ServerException | InternalException | XmlParserException | InvalidResponseException |
                 InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InsufficientDataException e) {
            throw new RuntimeException(e);
        }
    }

}

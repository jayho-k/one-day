package jayho.oneday.service;


import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jayho.common.snowflake.Snowflake;
import jayho.oneday.config.MinioProperties;
import jayho.oneday.entity.Article;
import jayho.oneday.entity.SavedArticle;
import jayho.oneday.entity.User;
import jayho.oneday.repository.ArticleRepository;
import jayho.oneday.repository.SavedArticleRepository;
import jayho.oneday.repository.UserRepository;
import jayho.oneday.service.request.ArticleCreateRequest;
import jayho.oneday.service.request.ArticleUpdateRequest;
import jayho.oneday.service.response.ArticleResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final SavedArticleRepository savedArticleRepository;
    private final UserRepository userRepository;
    private final Snowflake snowflake = new Snowflake();

    // image 별도 분리 필요
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Transactional
    public Article createArticle(ArticleCreateRequest articleInfo) {

        return articleRepository.save(Article.create(
                snowflake.nextId(),
                articleInfo.getImages(),
                articleInfo.getContent(),
                articleInfo.getWriterId()
        ));
    }

    // test
    @Transactional
    public void uploadImage(MultipartFile articleImage) {
        String originalFilename = articleImage.getOriginalFilename();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(originalFilename)
                            .stream(articleImage.getInputStream(), articleImage.getSize(), -1)
                            .contentType(articleImage.getContentType())
                            .build()
            );
        } catch (IOException | ServerException | InternalException | XmlParserException | InvalidResponseException |
                 InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InsufficientDataException e) {
            throw new RuntimeException(e);
        }
    }

    public ArticleResponseData readArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        User user = userRepository.findById(article.getWriterId()).orElseThrow();
        return ArticleResponseData.from(article, user);
    }

    public List<ArticleResponseData> readAllArticle(Integer pageSize, Long lastArticleId) {
//        return articleRepository.findArticleResponseAll(pageSize, lastArticleId);
        return List.of();
    }

    @Transactional
    public ArticleResponseData updateArticle(ArticleUpdateRequest articleInfo) {
        Article article = articleRepository.findById(articleInfo.getArticleId()).orElseThrow();
        article.setImages(articleInfo.getImages());
        article.setContent(articleInfo.getContent());

        Article updatedArticle = articleRepository.save(article);
        User user = userRepository.findById(updatedArticle.getWriterId()).orElseThrow();
        return ArticleResponseData.from(updatedArticle, user);
    }

    @Transactional
    public Article tmpDeleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.setIsDelete(true);
        return articleRepository.save(article);
    }

    @Transactional
    public Long deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
        return articleId;
    }

    @Transactional
    public SavedArticle saveCollectArticle(Long articleId, Long userId) {
        return savedArticleRepository.save(
                SavedArticle.create(snowflake.nextId(), articleId, userId)
        );
    }
}

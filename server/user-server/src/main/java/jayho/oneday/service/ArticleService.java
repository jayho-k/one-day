package jayho.oneday.service;


import jayho.common.snowflake.Snowflake;
import jayho.oneday.entity.Article;
import jayho.oneday.entity.SavedArticle;
import jayho.oneday.entity.User;
import jayho.oneday.repository.*;
import jayho.oneday.service.request.ArticleCreateRequest;
import jayho.oneday.service.request.ArticleImageUploadRequest;
import jayho.oneday.service.request.ArticleUpdateRequest;
import jayho.oneday.service.response.ArticleImageResponseData;
import jayho.oneday.service.response.ArticleResponseData;
import jayho.oneday.entity.ArticleImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ArticleService {

    private final Snowflake snowflake;
    private final PresignedUrlService presignedUrlService;

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final SavedArticleRepository savedArticleRepository;


    @Transactional
    public Article createArticle(ArticleCreateRequest articleInfo) {
        Long articleId = snowflake.nextId();

        List<ArticleImage> articleImages = articleImageRepository.saveAll(articleInfo.getImages().stream().peek(
                articleImage -> articleImage.setArticleId(articleId)).toList());

        if (articleImages.size() != articleInfo.getImages().size()) {
            throw new RuntimeException("실패한 image를 저장하는데 실패했습니다.");
        }

        return articleRepository.save(Article.create(
                articleId,
                articleInfo.getContent(),
                articleInfo.getWriterId()
        ));
    }


    public ArticleImageResponseData getUploadPresignedUrl(ArticleImageUploadRequest request) {
        return ArticleImageResponseData.from(
                ArticleImage.create(
                        snowflake.nextId(),
                        request.getArticleImageName()),
                presignedUrlService.getUploadPresignedUrl(request.getArticleImageName())
        );
    }

    public ArticleResponseData readArticle(Long articleId) {

        Article article = articleRepository.findById(articleId).orElseThrow();
        List<ArticleImage> articleImageList = articleImageRepository.findByArticleId(articleId);
        User user = userRepository.findById(article.getWriterId()).orElseThrow();

        return ArticleResponseData.from(article, user, articleImageList);
    }

    public List<ArticleResponseData> readAllArticle(Integer pageSize, Long lastArticleId) {
//        return articleRepository.findArticleResponseAll(pageSize, lastArticleId);
        return List.of();
    }

    @Transactional
    public ArticleResponseData updateArticle(ArticleUpdateRequest articleInfo) {

        Article article = articleRepository.findById(articleInfo.getArticleId()).orElseThrow();
        article.setContent(articleInfo.getContent());
        Article updatedArticle = articleRepository.save(article);
        User user = userRepository.findById(updatedArticle.getWriterId()).orElseThrow();
        return ArticleResponseData.from(updatedArticle, user, articleInfo.getImages());
    }

    @Transactional
    public void updateArticleImage(ArticleUpdateRequest articleInfo) {

        Set<Long> originImageIdSet = articleImageRepository.findByArticleId(articleInfo.getArticleId()).stream()
                .map(ArticleImage::getArticleImageId)
                .collect(Collectors.toSet());

        Map<Boolean, List<ArticleImage>> deleteCandidateImageMap = articleInfo.getImages().stream()
                .collect(Collectors.partitioningBy(
                        articleImage -> originImageIdSet.contains(articleImage.getArticleImageId())
                ));

        List<ArticleImage> deleteArticleImage = deleteCandidateImageMap.get(false).stream()
                .filter(articleImage -> originImageIdSet.contains(articleImage.getArticleImageId()))
                .peek(articleImage -> articleImage.setDelete(false)).toList();

        articleImageRepository.saveAll(articleInfo.getImages());
        articleImageRepository.updateAll(deleteArticleImage);

        // return 필요

    }

    @Transactional
    public Article tmpDeleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.setIsDelete(true);
        return articleRepository.save(article);
    }

    public Long deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
        return articleId;
    }

    public SavedArticle saveCollectArticle(Long articleId, Long userId) {
        return savedArticleRepository.save(
                SavedArticle.create(snowflake.nextId(), articleId, userId)
        );
    }
}

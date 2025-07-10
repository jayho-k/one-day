package jayho.userserver.service;

import jayho.common.snowflake.Snowflake;
import jayho.useractiverdb.entity.Article;
import jayho.useractiverdb.entity.SavedArticle;
import jayho.useractiverdb.entity.User;
import jayho.useractiverdb.repository.ArticleRepository;
import jayho.useractiverdb.repository.SavedArticleRepository;
import jayho.useractiverdb.repository.UserRepository;
import jayho.userserver.service.request.ArticleCreateRequest;
import jayho.userserver.service.request.ArticleUpdateRequest;
import jayho.userserver.service.response.ArticleResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final SavedArticleRepository savedArticleRepository;
    private final UserRepository userRepository;
    private final Snowflake snowflake = new Snowflake();

    public Article createArticle(ArticleCreateRequest articleInfo) {
        return articleRepository.save(Article.create(
                snowflake.nextId(),
                articleInfo.getImages(),
                articleInfo.getContent(),
                articleInfo.getWriterId()
        ));
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

    public ArticleResponseData updateArticle(ArticleUpdateRequest articleInfo) {

        Article article = articleRepository.findById(articleInfo.getArticleId()).orElseThrow();
        article.setImages(articleInfo.getImages());
        article.setContent(articleInfo.getContent());

        Article updatedArticle = articleRepository.save(article);
        User user = userRepository.findById(updatedArticle.getWriterId()).orElseThrow();

        return ArticleResponseData.from(updatedArticle, user);
    }

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

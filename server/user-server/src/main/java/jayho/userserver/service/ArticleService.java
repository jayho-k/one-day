package jayho.userserver.service;

import jayho.userserver.entity.Article;
import jayho.userserver.entity.SavedArticle;
import jayho.userserver.entity.User;
import jayho.userserver.repository.ArticleRepository;
import jayho.userserver.repository.SavedArticleRepository;
import jayho.userserver.repository.UserRepository;
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

    public void createArticle(ArticleCreateRequest articleInfo) {
        articleRepository.save(Article.create(
                articleInfo.getImages(),
                articleInfo.getContent(),
                articleInfo.getWriterId()
        ));
    }

    public ArticleResponseData readArticle(Long articleId) {
        return articleRepository.findArticleResponseById(articleId);
    }

    public List<ArticleResponseData> readAllArticle(Integer pageSize, Long lastArticleId) {
        return articleRepository.findArticleResponseAll(pageSize, lastArticleId);
    }

    public ArticleResponseData updateArticle(ArticleUpdateRequest articleInfo) {
        Article article = articleRepository.findById(articleInfo.getArticleId()).orElseThrow();
        article.update(articleInfo.getImages(), articleInfo.getContent());
        articleRepository.save(article).orElseThrow();
        return articleRepository.findArticleResponseById(article.getArticleId());
    }

    public void tmpDeleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        article.tmpDelete(true);
        articleRepository.save(article).orElseThrow();
    }

    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public void saveArticle(Long articleId, Long userId) {
        savedArticleRepository.save(
                SavedArticle.create(articleId, userId)
        );
    }
}

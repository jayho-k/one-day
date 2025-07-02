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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SavedArticleRepository savedArticleRepository;


    @Test
    void createArticle() {

        Long writerId = 1L;
        List<String> images = List.of("image1.png", "image2.png", "image3.png");
        String content = "content";

        ArticleCreateRequest request = new ArticleCreateRequest(images, content, writerId);

        articleService.createArticle(request);
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void readArticle() {

        Long articleId = 1L;
        Long writerId = 1L;
        List<String> images = List.of("image1.png", "image2.png", "image3.png");
        String content = "content";

        // given
        Article article = createArticle(articleId, images, content, writerId);
        User user = User.create(article.getWriterId(), "jayho", "profile.png");
        ArticleResponseData will = ArticleResponseData.from(article, user);
        given(articleRepository.findArticleResponseById(articleId)).willReturn(will);

        // when
        ArticleResponseData response = articleService.readArticle(articleId);

        // then
        assertThat(response).isEqualTo(will);
    }

    Article createArticle(Long articleId, List<String> images, String content, Long writerId) {
        Article article = Article.create(images, content, writerId);
        article.assignId(articleId);
        return article;
    }


    @Test
    void updateArticle() {

        // given
        Long articleId = 1L;
        Long writerId = 1L;

        Article oldArticle = Article.create(List.of("oldImage.png"), "originContent", writerId);
        oldArticle.assignId(articleId); // ID 할당

        Article newArticle = Article.create(List.of("newImage.png"), "newContent", writerId);
        User user = User.create(writerId, "jayho", "profile.png");

        newArticle.assignId(articleId); // ID 할당

        ArticleUpdateRequest request = new ArticleUpdateRequest(articleId, List.of("newImage.png"), "newContent");
        given(articleRepository.findById(articleId)).willReturn(Optional.of(oldArticle));
        given(articleRepository.save(any(Article.class))).willReturn(Optional.of(oldArticle));
        given(articleRepository.findArticleResponseById(request.getArticleId())).willReturn(ArticleResponseData.from(newArticle, user));

        // when
        ArticleResponseData result = articleService.updateArticle(request);

        // then
        verify(articleRepository).save(any(Article.class));
        assertThat(articleRepository.findArticleResponseById(articleId)).isEqualTo(result);
    }



    @Test
    void tmpDeleteArticle() {
        // given
        Long articleId = 1L;
        Long writerId = 1L;

        Article article = Article.create(List.of("image.png"), "content", writerId);
        article.tmpDelete(true);

        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
        given(articleRepository.save(any(Article.class))).willReturn(Optional.of(article));

        // when
        articleService.tmpDeleteArticle(articleId);

        // then
        verify(articleRepository).save(any(Article.class));
        assertThat(article.getIsDelete()).isTrue();
    }

    @Test
    void deleteArticle() {
        // given
        Long articleId = 1L;

        // when
        articleService.deleteArticle(articleId);

        // then
        verify(articleRepository).deleteById(articleId);
    }

    @Test
    void saveArticle_success() {
        // given
        Long articleId = 1L;
        Long userId = 1L;

        // when
        articleService.saveArticle(articleId, userId);

        // then
        verify(savedArticleRepository).save(any(SavedArticle.class));
    }
}
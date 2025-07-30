package jayho.oneday.service;

import jayho.common.snowflake.Snowflake;
import jayho.oneday.entity.Article;
import jayho.oneday.entity.ArticleImage;
import jayho.oneday.entity.SavedArticle;
import jayho.oneday.entity.User;
import jayho.oneday.repository.ArticleImageRepository;
import jayho.oneday.repository.ArticleRepository;
import jayho.oneday.repository.SavedArticleRepository;
import jayho.oneday.repository.UserRepository;
import jayho.oneday.service.request.ArticleCreateRequest;
import jayho.oneday.service.request.ArticleUpdateRequest;
import jayho.oneday.service.response.ArticleResponseData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    @Mock
    private ArticleImageRepository articleImageRepository;
    @Mock
    private Snowflake snowflake;


    @Test
    void createArticle() {

        Long writerId = 1L;
        List<ArticleImage> images = IntStream.range(0, 10)
                .mapToObj(i -> ArticleImage.create((long) i, String.format("test-image%s.png", i)))
                .collect(Collectors.toList());
        String content = "content";

        ArticleCreateRequest request = new ArticleCreateRequest(images, content, writerId);

        given(snowflake.nextId()).willReturn(1L);
        given(articleImageRepository.saveAll(any())).willReturn(images);

        articleService.createArticle(request);
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void readArticle() {

        Long articleId = 1L;
        Long writerId = 1L;
        List<ArticleImage> images = IntStream.range(0, 10)
                .mapToObj(i -> ArticleImage.create((long) i, String.format("test-image%s.png", i)))
                .collect(Collectors.toList());
        String content = "content";

        // given
        Article article = createArticle(articleId, content, writerId);
        User user = User.create(article.getWriterId(), "jayho", "profile.png");
        ArticleResponseData will = ArticleResponseData.from(article, user, images);

        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
        given(articleImageRepository.findByArticleId(article.getWriterId())).willReturn(images);
        given(userRepository.findById(article.getWriterId())).willReturn(Optional.of(user));

        // when
        ArticleResponseData response = articleService.readArticle(articleId);

        // then
        assertThat(response.getArticleId()).isEqualTo(will.getArticleId());
        assertThat(response.getArticleImageList()).isEqualTo(will.getArticleImageList());
        assertThat(response.getContent()).isEqualTo(will.getContent());
        assertThat(response.getWriterId()).isEqualTo(will.getWriterId());
        assertThat(response.getWriterProfileImage()).isEqualTo(will.getWriterProfileImage());
    }

    @Test
    void readAllArticle() {



    }

    Article createArticle(Long articleId, String content, Long writerId) {
        return Article.create(articleId, content, writerId);
    }


    @Test
    void updateArticle() {

        // given
        Long articleId = 1L;
        Long writerId = 1L;

        Article oldArticle = Article.create(articleId, "originContent", writerId);

        Article newArticle = Article.create(articleId, "newContent", writerId);
        User user = User.create(writerId, "jayho", "profile.png");

        List<ArticleImage> images = IntStream.range(0, 10)
                .mapToObj(i -> ArticleImage.create((long) i, String.format("test-image%s.png", i)))
                .collect(Collectors.toList());

        ArticleUpdateRequest request = new ArticleUpdateRequest(articleId, images, "newContent");
        given(articleRepository.findById(articleId)).willReturn(Optional.of(oldArticle));
        given(articleRepository.save(any(Article.class))).willReturn(newArticle);
        given(userRepository.findById(writerId)).willReturn(Optional.of(user));

        ArticleResponseData will = ArticleResponseData.from(newArticle, user, images);

        // when
        ArticleResponseData result = articleService.updateArticle(request);

        // then
        verify(articleRepository).save(any(Article.class));
        assertThat(will.getArticleId()).isEqualTo(result.getArticleId());
        assertThat(will.getArticleImageList()).isEqualTo(result.getArticleImageList());
        assertThat(will.getContent()).isEqualTo(result.getContent());
        assertThat(will.getWriterId()).isEqualTo(result.getWriterId());
        assertThat(will.getWriterProfileImage()).isEqualTo(result.getWriterProfileImage());
    }



    @Test
    void tmpDeleteArticle() {
        // given
        Long articleId = 1L;
        Long writerId = 1L;

        Article article = Article.create(articleId, "content", writerId);
        article.setIsDelete(true);

        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
        given(articleRepository.save(any(Article.class))).willReturn(article);

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
        given(snowflake.nextId()).willReturn(1L);
        articleService.saveCollectArticle(articleId, userId);

        // then
        verify(savedArticleRepository).save(any(SavedArticle.class));
    }
}
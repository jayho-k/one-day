package jayho.userserver.data;

import jayho.userserver.entity.Article;
import jayho.userserver.entity.User;
import jayho.userserver.repository.ArticleRepository;
import jayho.userserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataInitialize {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public void initArticle() {

        IntStream.range(0, 10).forEach(i -> {
            Article article = Article.create(List.of(String.format("image%s.png", i)), String.format("content%s", i), (long) i);
            article.assignId((long) i);
            articleRepository.save(article);
            userRepository.save(User.create((long)i, String.format("user%s", i), String.format("profile%s.png", i)));
        });

    }

    public void clearArticle() {
        articleRepository.findAll().forEach(article -> articleRepository.deleteById(article.getArticleId()));
        userRepository.findAll().forEach(user -> userRepository.deleteById(user.getUserId()));
    }


}

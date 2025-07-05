package jayho.userserver.repository;

import jayho.userserver.entity.Article;
import jayho.userserver.entity.User;
import jayho.userserver.service.response.ArticleResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ConcurrentNavigableMap<Long, Article> repository = new ConcurrentSkipListMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    private final UserRepository userRepository;

    @Override
    public Optional<Article> save(Article article) {
        if (article.getArticleId() == null) {
            article.assignId(sequence.getAndIncrement());
        }
        repository.put(article.getArticleId(), article);
        return Optional.of(article);
    }

    @Override
    public Optional<Article> findById(Long articleId) {
        return Optional.ofNullable(repository.get(articleId));
    }

    @Override
    public ArticleResponseData findArticleResponseById(Long articleId) {
        Article article = repository.get(articleId);
        return ArticleResponseData.from(
                article,
                userRepository.findById(article.getWriterId()).orElseThrow());
    }

    @Override
    public List<Article> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public List<ArticleResponseData> findArticleResponseAll(Integer pageSize,  Long lastArticleId) {
        List<Article> articleList = new ArrayList<>();
        if (lastArticleId == null) {
            return repository.values().stream().limit(pageSize)
                    .map(article ->
                            ArticleResponseData.from(
                                    article, userRepository.findById(article.getWriterId()).orElseThrow()
                            )
                    ).toList();
        }
        return repository.subMap(lastArticleId, lastArticleId + Long.valueOf(pageSize))
                .values().stream().limit(pageSize)
                .map(article ->
                        ArticleResponseData.from(
                                article, userRepository.findById(article.getWriterId()).orElseThrow()
                        )
                ).toList();
    }


    @Override
    public void deleteById(Long articleId) {
        repository.remove(articleId);
    }

}

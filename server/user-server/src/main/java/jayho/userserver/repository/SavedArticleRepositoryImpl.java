package jayho.userserver.repository;

import jayho.userserver.entity.Article;
import jayho.userserver.entity.SavedArticle;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SavedArticleRepositoryImpl implements SavedArticleRepository{

    private final static ConcurrentMap<Long, SavedArticle> repository = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    @Override
    public void save(SavedArticle savedArticle) {
        repository.put(sequence.getAndIncrement(), savedArticle);
    }

    @Override
    public Optional<SavedArticle> findById(Long savedArticleId) {
        return Optional.ofNullable(repository.get(savedArticleId));
    }

    @Override
    public List<SavedArticle> findAll(Long userId) {
        return repository.values().stream()
                .filter(savedArticle
                        -> savedArticle.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void deleteById(Long articleId) {
        repository.remove(articleId);
    }
}

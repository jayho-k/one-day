package jayho.userserver.repository;

import jayho.userserver.entity.Article;
import jayho.userserver.entity.Comment;
import jayho.userserver.service.response.ArticleResponseData;
import jayho.userserver.service.response.CommentResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{

    private final ConcurrentMap<Long, Comment> repository = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0);

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;


    @Override
    public Optional<Comment> save(Comment comment) {
        if (comment.getCommentId() == null) {
            comment.assignId(sequence.getAndIncrement());
        }
        repository.put(comment.getCommentId(), comment);
        return Optional.of(comment);
    }

    @Override
    public Optional<Comment> findById(Long articleId) {
        return Optional.ofNullable(repository.get(articleId));
    }

    @Override
    public CommentResponseData findCommentResponseById(Long commentId) {
        Comment comment = repository.get(commentId);
        return CommentResponseData.from(
                comment,
                userRepository.findById(comment.getWriterId()).orElseThrow()
        );
    }

    @Override
    public List<CommentResponseData> findCommentResponseByArticleId(Long articleId, Long lastCommentId, Integer pageSize) {
        List<Comment> commentList = repository.values().stream()
                .filter(comment -> comment.getArticleId().equals(articleId)).sorted()
                .toList();
        if (lastCommentId == null) {
            return commentList.stream().limit(pageSize)
                    .map(comment ->
                    CommentResponseData.from(
                            comment,
                            userRepository.findById(comment.getWriterId()).orElseThrow()
                    )).toList();
        }

        long cnt = IntStream.range(0, commentList.size())
                .filter(i -> commentList.get(i).getCommentId().equals(lastCommentId))
                .findFirst()
                .orElseThrow();

        return commentList.stream().skip(cnt).limit(pageSize)
                .map(comment ->
                        CommentResponseData.from(
                                comment,
                                userRepository.findById(comment.getWriterId()).orElseThrow()
                        )).toList();
    }

    @Override
    public void deleteById(Long commentId) {

    }
}

package jayho.userserver.service;

import jakarta.validation.constraints.NotNull;
import jayho.userserver.entity.Comment;
import jayho.userserver.entity.User;
import jayho.userserver.repository.ArticleRepository;
import jayho.userserver.repository.CommentRepository;
import jayho.userserver.repository.SavedArticleRepository;
import jayho.userserver.repository.UserRepository;
import jayho.userserver.service.request.CommentCreateRequest;
import jayho.userserver.service.response.CommentResponseData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Test
    void createComment() {

        Long articleId = 1L;
        String content = "content1";
        Long writerId = 1L;
        CommentCreateRequest request = new CommentCreateRequest(articleId, content, writerId);

        commentService.createComment(request);
        verify(commentRepository).save(any(Comment.class));

    }

    @Test
    void readCommentByArticle() {
        // given
        Long articleId = 1L;
        User user = User.create(1L, "user1", "image.jpg");

        int pageSize = 5;
        List<CommentResponseData> commentList = IntStream.range(0, pageSize)
            .mapToObj(i -> CommentResponseData.from(
                Comment.create(String.format("comment%d", i), user.getUserId(), articleId), user))
            .toList();
        given(commentRepository.findCommentResponseByArticleId(articleId, null, pageSize)).willReturn(commentList);

        // when
        List<CommentResponseData> responsesList = commentService.readAllScroll(articleId, null, pageSize);

        // then
        assertThat(responsesList).hasSize(pageSize);
        assertThat(responsesList).isEqualTo(commentList);
        verify(commentRepository).findCommentResponseByArticleId(articleId, null, pageSize);
    }


    @Test
    void updateComment() {
        // given
        Long commentId = 1L;
        Long writerId = 1L;

    }

    @Test
    void deleteComment() {

    }
}

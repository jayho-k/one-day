package jayho.oneday.service;

import jayho.oneday.adaptor.producer.ArticleLikeProducer;
import jayho.oneday.entity.ArticleLike;
import jayho.oneday.entity.ArticleLikeCount;
import jayho.oneday.entity.id.LikeId;
import jayho.oneday.event.ArticleLikeCountEvent;
import jayho.oneday.repository.ArticleLikeCountRepository;
import jayho.oneday.repository.ArticleLikeMemoryRepository;
import jayho.oneday.repository.ArticleLikeRepository;
import jayho.oneday.service.response.ArticleLikeCountResponseData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ArticleLikeServiceTest {

    @InjectMocks
    private ArticleLikeService articleLikeService;

    @Mock
    private ArticleLikeRepository articleLikeRepository;
    @Mock
    private ArticleLikeCountRepository articleLikeCountRepository;
    @Mock
    private ArticleLikeMemoryRepository articleLikeMemoryRepository;
    @Mock
    private ArticleLikeReadService articleLikeReadService;
    @Mock
    private ArticleLikeProducer articleLikeProducer;


    @Test
    @DisplayName("게시글 좋아요 요청 시 MQ로 메시지를 전송한다")
    void articleLikeToMQ_Success() {
        // given
        Long articleId = 1L;
        Long userId = 100L;

        // when
        articleLikeService.articleLikeToMQ(articleId, userId);

        // then
        // Producer의 articleLikeMessage 메소드가 정확한 인자(articleId, userId, isLiked=true)로 호출되었는지 검증
        verify(articleLikeProducer).articleLikeMessage(articleId, userId, true);
    }

    @Test
    @DisplayName("벌크 좋아요 메시지 처리 시, 성공 건은 카운팅하고 실패 건은 업데이트를 시도한다")
    void validateArticleLikeBulk_SuccessAndFailureMixed() {
        // given
        // 성공할 좋아요 2건, 실패(업데이트)할 좋아요 1건 준비
        ArticleLike success1 = ArticleLike.create(1L, 101L, true);
        ArticleLike success2 = ArticleLike.create(1L, 102L, true);
        ArticleLike failure1 = ArticleLike.create(2L, 201L, false);
        List<ArticleLike> allLikes = List.of(success1, success2, failure1);

        // saveAll의 결과로 성공(1), 성공(1), 실패(0)를 반환하도록 설정
        given(articleLikeRepository.saveAll(allLikes)).willReturn(new int[]{1, 1, 0});

        // 실패한 좋아요(failure1)에 대해 findById를 호출하면, 기존 데이터를 반환하도록 설정
        given(articleLikeRepository.findById(LikeId.create(failure1.getArticleId(), failure1.getUserId())))
                .willReturn(Optional.of(failure1));

        given(articleLikeRepository.save(failure1)).willReturn(failure1);

        // when
        articleLikeService.validateArticleLikeBulk(allLikes);

        // then
        // 1. 성공한 2건에 대한 카운트 이벤트가 MQ로 전송되었는지 검증
        ArgumentCaptor<Map<Long, ArticleLikeCountEvent>> mapCaptor = ArgumentCaptor.forClass(Map.class);
        verify(articleLikeProducer).articleLikeCountMessage(mapCaptor.capture());
        Map<Long, ArticleLikeCountEvent> capturedMap = mapCaptor.getValue();
        assertThat(capturedMap.get(1L).getCount()).isEqualTo(2); // articleId 1L에 대한 좋아요는 2건

        // 2. 실패한 1건에 대해 업데이트 로직이 수행되었는지 검증
        verify(articleLikeRepository).findById(LikeId.create(failure1.getArticleId(), failure1.getUserId()));
        verify(articleLikeRepository).save(failure1); // 업데이트를 위한 save 호출 검증
        verify(articleLikeProducer).articleLikeCountMessage(failure1.getArticleId(), 1L, true); // 단건 카운트 메시지 전송 검증
    }

    @Test
    @DisplayName("좋아요 카운트 메시지 수신 시, 기존 카운트가 있으면 더한다")
    void articleLikeCountingMQ_WhenCountExists() {
        // given
        Long existingArticleId = 1L;
        ArticleLikeCount existingCount = ArticleLikeCount.create(existingArticleId, 10L);
        given(articleLikeCountRepository.findById(existingArticleId)).willReturn(Optional.of(existingCount));

        // when
        articleLikeService.articleLikeCountingMQ(existingArticleId, true, 5L);

        // then
        assertThat(existingCount.getLikeCount()).isEqualTo(15L); // 10 + 5 = 15
        verify(articleLikeCountRepository).save(existingCount);
        verify(articleLikeMemoryRepository).setLikeCount(existingArticleId, 15L);
    }

    @Test
    @DisplayName("좋아요 카운트 메시지 수신 시, 기존 카운트가 없으면 새로 생성한다")
    void articleLikeCountingMQ_WhenCountNotExists() {
        // given
        Long newArticleId = 2L;
        given(articleLikeCountRepository.findById(newArticleId)).willReturn(Optional.empty());

        // when
        articleLikeService.articleLikeCountingMQ(newArticleId, true, 3L);

        // then
        ArgumentCaptor<ArticleLikeCount> newCountCaptor = ArgumentCaptor.forClass(ArticleLikeCount.class);
        verify(articleLikeCountRepository).save(newCountCaptor.capture());

        ArticleLikeCount capturedValue = newCountCaptor.getValue();
        assertThat(capturedValue.getArticleId()).isEqualTo(newArticleId);
        assertThat(capturedValue.getLikeCount()).isEqualTo(3L);
    }

    @Test
    @DisplayName("좋아요 취소 요청 시, 'liked' 상태를 false로 변경하여 MQ로 전송한다")
    void articleUnlikeMQ_Success() {
        // given
        Long articleId = 1L;
        Long userId = 100L;
        ArticleLike existingLike = ArticleLike.create(articleId, userId, true);
        given(articleLikeRepository.findById(LikeId.create(articleId, userId))).willReturn(Optional.of(existingLike));

        // when
        articleLikeService.articleUnlikeMQ(articleId, userId);

        // then
        assertThat(existingLike.getLiked()).isFalse(); // 엔티티의 상태가 false로 변경되었는지 확인
        verify(articleLikeProducer).articleLikeMessage(articleId, userId, false); // liked=false 메시지 전송 검증
    }

    @Test
    @DisplayName("좋아요 개수 조회 시, 캐시(ReadService)에서 정상적으로 값을 가져온다")
    void readLikeCount_Success() {
        // given
        Long articleId = 1L;
        given(articleLikeReadService.readLikeCount(articleId)).willReturn(123L);

        // when
        ArticleLikeCountResponseData response = articleLikeService.readLikeCount(articleId);
        // then
        assertThat(response.getCount()).isEqualTo(123L);
    }

    @Test
    @DisplayName("좋아요 개수 조회 시, 캐시(ReadService)가 음수를 반환하면 0으로 변환한다")
    void readLikeCount_WhenCacheReturnsNegative() {
        // given
        Long articleId = 1L;
        given(articleLikeReadService.readLikeCount(articleId)).willReturn(-1L); // 캐시 미스 등의 이유로 음수 반환 가정

        // when
        ArticleLikeCountResponseData response = articleLikeService.readLikeCount(articleId);

        // then
        assertThat(response.getCount()).isZero();
    }


}

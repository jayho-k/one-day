package jayho.oneday.service;

import jayho.oneday.entity.ArticleLikeCount;
import jayho.oneday.repository.ArticleLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleLikeReadService {

    private final ArticleLikeCountRepository articleLikeCountRepository;

    @Cacheable(value = "article::like", key = "'count::articleId::' + #articleId")
    public Long readLikeCount(Long articleId) {
        return articleLikeCountRepository.findById(articleId)
                .map(ArticleLikeCount::getLikeCount)
                .orElseThrow();
    }

}

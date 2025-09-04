package jayho.oneday.service;

import jayho.oneday.entity.Article;
import jayho.oneday.repository.ArticleRepository;
import jayho.oneday.repository.HotArticleMemoryRepository;
import jayho.oneday.service.response.ArticleResponseData;
import jayho.oneday.service.response.CommentResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotArticleService {

    private final ArticleRepository articleRepository;
    private final HotArticleMemoryRepository hotArticleRankingRepository; // memory

    private final static Integer limitNum = 100;
    private final static Integer viewScore = 1;
    private final static Integer likeCountScore = 2;

    // util로 topic name을 모으는 것이 좋을 거 같음
    // key: topicName, value:score
    Map<String, Integer> scoreWeightMap = Map.of(
            "topic-article-view", viewScore,
            "topic-article-like-count", likeCountScore
    );

    public void updateArticleScore(Long articleId, String topicName, Long score) {
        Double value = hotArticleRankingRepository.updateScore(
                articleId, calculateScore(topicName, score)
        );
        log.info("article score is {}",value);
    }
    

    public List<Article> getArticleTop(Long top) {
        if (top > limitNum) {
            log.info("too many article.");
        }
        List<Long> hotArticles = hotArticleRankingRepository.getHotArticle(10);
        return articleRepository.findByIds(hotArticles);
    }

    private Long calculateScore(String topicName, Long score) {
        return scoreWeightMap.get(topicName) * score;
    }

}

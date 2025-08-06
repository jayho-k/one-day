package jayho.oneday.repository;

import jakarta.persistence.Id;
import jayho.oneday.entity.ArticleImage;
import jayho.oneday.entity.ArticleViewCount;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleViewCountJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<ArticleViewCount> updateAll(List<ArticleViewCount> articleViewCountList) {

        String sql = "UPDATE article_view_count SET article_view=article_view+? WHERE article_id = ?";

        int[] resultArray = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ArticleViewCount articleViewCount = articleViewCountList.get(i);
                ps.setLong(1, articleViewCount.getArticleView());
                ps.setLong(2, articleViewCount.getArticleId());
            }

            @Override
            public int getBatchSize() {
                return articleViewCountList.size();
            }
        });
        // update에 실패한 값들 list return -> insert를 위함
        return Arrays.stream(resultArray)
                .filter(i -> i < 1)
                .mapToObj(articleViewCountList::get)
                .toList();
    }


}

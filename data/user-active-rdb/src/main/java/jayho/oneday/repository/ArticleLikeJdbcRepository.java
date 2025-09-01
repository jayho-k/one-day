package jayho.oneday.repository;

import jayho.oneday.entity.ArticleLike;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleLikeJdbcRepository {

    private final JdbcTemplate jdbcTemplate;


    // insert into article_like(article_id, user_id, liked,created_at, modified_at) values (1,1,true ,now(), now());
    public int[] saveAll(List<ArticleLike> articleLikeList) {

        String sql = "INSERT INTO ARTICLE_LIKE (article_id, user_id, liked, created_at, modified_at) VALUES (?,?,?,?,?) ON CONFLICT DO NOTHING";
        int[] resultArray = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ArticleLike articleLike = articleLikeList.get(i);
                ps.setLong(1, articleLike.getArticleId());
                ps.setLong(2, articleLike.getUserId());
                ps.setBoolean(3, articleLike.getLiked());
                ps.setTimestamp(4, Timestamp.valueOf(articleLike.getCreatedAt()));
                ps.setTimestamp(5, Timestamp.valueOf(articleLike.getModifiedAt()));
            }

            @Override
            public int getBatchSize() {
                return articleLikeList.size();
            }
        });
        return resultArray;
    }










}

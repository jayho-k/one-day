package jayho.oneday.repository;


import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jayho.oneday.entity.ArticleImage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ArticleImageJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<ArticleImage> saveAll(List<ArticleImage> articleImageList) {

        String sql = "INSERT INTO ARTICLE_IMAGE (article_image_id, article_id, article_image_name, created_at) VALUES (?,?,?,?)";
        int[] resultArray = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ArticleImage articleImage = articleImageList.get(i);
                ps.setLong(1, articleImage.getArticleImageId());
                ps.setLong(2, articleImage.getArticleId());
                ps.setString(3, articleImage.getArticleImageName());
                ps.setTimestamp(4, Timestamp.valueOf(articleImage.getCreatedAt()));
            }

            @Override
            public int getBatchSize() {
                return articleImageList.size();
            }
        });
        return Arrays.stream(resultArray)
                .filter(i -> i >= 1)
                .mapToObj(articleImageList::get)
                .toList();

    }


    public List<ArticleImage> updateAll(List<ArticleImage> articleImageList) {

        String sql = "UPDATE article_image SET article_image_name=?, delete=? WHERE article_image_id = ?";

        int[] resultArray = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ArticleImage articleImage = articleImageList.get(i);
                ps.setLong(1, articleImage.getArticleImageId());
                ps.setBoolean(2, articleImage.getDelete());
                ps.setLong(3, articleImage.getArticleImageId());
            }

            @Override
            public int getBatchSize() {
                return articleImageList.size();
            }
        });

        return Arrays.stream(resultArray)
                .filter(i -> i >= 1)
                .mapToObj(articleImageList::get)
                .toList();
    }




}

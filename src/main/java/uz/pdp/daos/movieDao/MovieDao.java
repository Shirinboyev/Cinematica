package uz.pdp.daos.movieDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.daos.BaseDao;
import uz.pdp.model.Movie;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Component
public class MovieDao implements BaseDao<Movie> {

    private final JdbcTemplate jdbcTemplate;

    public MovieDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Movie> rowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = convertToLocalDate(rs.getTimestamp("release_date"));
        LocalDate endTime = convertToLocalDate(rs.getTimestamp("end_time"));
        String categoryName = rs.getString("category_name");
        String posterImage = rs.getString("poster_image");
        LocalDate createDate = convertToLocalDate(rs.getTimestamp("create_date"));

        return Movie.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .endTime(endTime)
                .categoryName(categoryName)
                .posterImage(posterImage)
                .createDate(createDate)
                .build();
    };

    @Override
    public void save(Movie entity) {
        String sql = "INSERT INTO movies(name, description, release_date, end_time, category_name, poster_image, create_date) VALUES(:name, :description, :release_date, :end_time, :category_name, :poster_image, :create_date)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", entity.getName())
                .addValue("description", entity.getDescription())
                .addValue("release_date", convertToTimestamp(entity.getReleaseDate()))
                .addValue("end_time", convertToTimestamp(entity.getEndTime()))
                .addValue("category_name", entity.getCategoryName())
                .addValue("poster_image", entity.getPosterImage())
                .addValue("create_date", convertToTimestamp(entity.getCreateDate()));

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void update(Movie entity) {
        String sql = "UPDATE movies SET name=:name, description=:description, release_date=:release_date, end_time=:end_time, category_name=:category_name, poster_image=:poster_image, create_date=:create_date WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", entity.getName())
                .addValue("description", entity.getDescription())
                .addValue("release_date", convertToTimestamp(entity.getReleaseDate()))
                .addValue("end_time", convertToTimestamp(entity.getEndTime()))
                .addValue("category_name", entity.getCategoryName())
                .addValue("poster_image", entity.getPosterImage())
                .addValue("create_date", convertToTimestamp(entity.getCreateDate()))
                .addValue("id", entity.getId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(int entity) {
        String sql = "DELETE FROM movies WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", entity);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public Movie getById(int id) {
        String sql = "SELECT * FROM movies WHERE id=?";
        return jdbcTemplate.queryForObject(sql,rowMapper,id);
    }

    @Override
    public List<Movie> getAll() {
        String sql = "SELECT * FROM movies";
        return jdbcTemplate.query(sql, rowMapper);
    }

    private Timestamp convertToTimestamp(LocalDate localDate) {
        return localDate == null ? null : Timestamp.valueOf(localDate.atStartOfDay());
    }

    private LocalDate convertToLocalDate(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime().toLocalDate();
    }
}

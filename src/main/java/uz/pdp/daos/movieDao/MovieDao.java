package uz.pdp.daos.movieDao;
import org.apache.commons.codec.binary.Base64;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.daos.BaseDao;
import uz.pdp.imageEncoder.ImageEncoder;
import uz.pdp.model.Movie;

import java.io.IOException;
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
        int duration=rs.getInt("duration");
        String categoryName = rs.getString("category_name");
        String posterImage = rs.getString("poster_image");
        LocalDate createDate = convertToLocalDate(rs.getTimestamp("create_date"));

        return Movie.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .categoryName(categoryName)
                .posterImage(posterImage)
                .createDate(createDate)
                .build();
    };

    @Override
    public void save(Movie movie) throws IOException {
        String sql = "INSERT INTO movies(name, description, release_date, category_name, poster_image, create_date, duration,trailer_url) " +
                "VALUES(:name, :description, :release_date, :category_name, :poster_image, :create_date, :duration, :trailer_url)";

        String base64Image = movie.getPosterImage();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", movie.getName())
                .addValue("description", movie.getDescription())
                .addValue("release_date", convertToTimestamp(movie.getReleaseDate()))
                .addValue("category_name", movie.getCategoryName())
                .addValue("poster_image", base64Image)
                .addValue("create_date", convertToTimestamp(movie.getCreateDate()))
                .addValue("duration", movie.getDuration())
                .addValue("trailer_url",movie.getTrailer_url());

        jdbcTemplate.update(sql, params);
    }


    private String convertToBase64(String file) throws IOException {
        byte[] fileContent = file.getBytes();
        return Base64.encodeBase64String(fileContent);
    }


    @Override
    public void update(Movie entity) {
        String sql = "UPDATE movies SET name=:name, description=:description, release_date=:release_date,category_name=:category_name, poster_image=:poster_image, create_date=:create_date, trailer_url=:trailer_url WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", entity.getName())
                .addValue("description", entity.getDescription())
                .addValue("release_date", convertToTimestamp(entity.getReleaseDate()))
                .addValue("category_name", entity.getCategoryName())
                .addValue("poster_image", entity.getPosterImage())
                .addValue("create_date", convertToTimestamp(entity.getCreateDate()))
                .addValue("duration", entity.getDuration())
                .addValue("trailer_url",entity.getTrailer_url())
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
        String sql = "SELECT * FROM movies WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
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

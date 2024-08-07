package uz.pdp.daos.movieDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.daos.BaseDao;
import uz.pdp.model.Movie;

import java.time.LocalDate;
import java.util.List;

@Component
public class MovieDao implements BaseDao<Movie> {

    private final JdbcTemplate jdbcTemplate;

    public MovieDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Movie> rowMapper = (rs,rowMapper) -> {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer categoryId = rs.getInt("category_id");
        String posterUrl = rs.getString("poster_url");
        LocalDate createDate = rs.getDate("create_date").toLocalDate();
        return Movie.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .categoryId(categoryId)
                .posterImage(posterUrl)
                .createDate(createDate)
                .build();
    };


    @Override
    public void save(Movie entity) {
        String sql = "INSERT INTO movies(id,name,description,release_date,category_id,poster_image,create_date) VALUES(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,entity.getId(),entity.getName(),entity.getDescription(),entity.getReleaseDate(),entity.getCategoryId(),entity.getPosterImage(),entity.getCreateDate());

    }

    @Override
    public void update(Movie entity) {
        String sql = "UPDATE movies SET name=?,description=?,release_date=?,category_id=?,poster_image=?,create_date=? WHERE id=?";
        jdbcTemplate.update(sql, entity.getName(),entity.getDescription(),entity.getReleaseDate(),entity.getCategoryId(),entity.getPosterImage(),entity.getCreateDate(),entity.getId());

    }

    @Override
    public void delete(int entity) {
        String sql = "DELETE FROM movies WHERE id=?";
        jdbcTemplate.update(sql,entity);

    }

    @Override
    public Movie getById(int id) {
        String sql = "SELECT * FROM movies WHERE id=?";
        return jdbcTemplate.queryForObject(sql,rowMapper,id);
    }

    @Override
    public List<Movie> getAll() {
        String sql = "SELECT * FROM movies";
        return jdbcTemplate.query(sql,rowMapper);
    }
}

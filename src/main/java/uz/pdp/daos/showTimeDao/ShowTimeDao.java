package uz.pdp.daos.showTimeDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.daos.BaseDao;
import uz.pdp.model.ShowTime;
import uz.pdp.model.ShowTimeDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ShowTimeDao implements BaseDao<ShowTime> {

    private final JdbcTemplate jdbcTemplate;

    public ShowTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ShowTime> showTimeRowMapper = new RowMapper<>() {
        @Override
        public ShowTime mapRow(ResultSet rs, int rowNum) throws SQLException {
            return ShowTime.builder()
                    .id(rs.getInt("id"))
                    .screenId(rs.getInt("screen_id"))
                    .movieId(rs.getInt("movie_id"))
                    .showTime(rs.getTimestamp("show_time"))
                    .price(rs.getInt("price"))
                    .build();
        }
    };
    private final RowMapper<ShowTimeDetails> showTimeDetailsRowMapper = new RowMapper<>() {
        @Override
        public ShowTimeDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ShowTimeDetails(
                    ShowTime.builder()
                            .id(rs.getInt("id"))
                            .screenId(rs.getInt("screen_id"))
                            .movieId(rs.getInt("movie_id"))
                            .showTime(rs.getTimestamp("show_time"))
                            .price(rs.getInt("price"))
                            .build(),
                    rs.getString("movie_name"),
                    rs.getString("cinema_name")
            );
        }
    };

    @Override
    public void save(ShowTime entity) {
        String sql = "INSERT INTO showtime (screen_id, movie_id, show_time, price) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                entity.getScreenId(),
                entity.getMovieId(),
                entity.getShowTime(),
                entity.getPrice());
    }

    @Override
    public void update(ShowTime entity) {
        String sql = "UPDATE showtime SET screen_id = ?, movie_id = ?, show_time = ?, price = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                entity.getScreenId(),
                entity.getMovieId(),
                entity.getShowTime(),
                entity.getPrice(),
                entity.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM showtime WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public ShowTime getById(int id) {
        String sql = "SELECT * FROM showtime WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, showTimeRowMapper, id);
    }

    @Override
    public List<ShowTime> getAll() {
        String sql = "SELECT * FROM showtime";
        return jdbcTemplate.query(sql, showTimeRowMapper);
    }

    public List<ShowTime> getShowTimesByMovieId(int movieId) {
        String sql = "SELECT * FROM showtime WHERE movie_id = ?";
        return jdbcTemplate.query(sql, showTimeRowMapper, movieId);
    }
    public ShowTimeDetails getShowTimeDetailsById(int showtimeId) {
        String sql = "SELECT st.id, st.screen_id, st.movie_id, st.show_time, st.price, " +
                "m.name AS movie_name, sc.name AS cinema_name " +
                "FROM showtime st " +
                "INNER JOIN movies m ON st.movie_id = m.id " +
                "INNER JOIN screens sc ON st.screen_id = sc.id " +
                "WHERE st.id = ?";
        return jdbcTemplate.queryForObject(sql, showTimeDetailsRowMapper, showtimeId);
    }
    public boolean existsByMovieId(int movieId) {
        String sql = "SELECT COUNT(*) FROM showtime WHERE movie_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{movieId}, Integer.class);
        return count != null && count > 0;
    }
}

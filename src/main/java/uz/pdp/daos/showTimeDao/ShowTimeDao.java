package uz.pdp.daos.showTimeDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.daos.BaseDao;
import uz.pdp.model.ShowTime;

import java.util.List;

@Component
public class ShowTimeDao implements BaseDao<ShowTime> {

    private final JdbcTemplate jdbcTemplate;

    public ShowTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ShowTime> showtime = (rs, rowMapper) -> {
        int id = rs.getInt("id");
        return ShowTime.builder().id(id).build();
    };


    @Override
    public void save(ShowTime entity) {
        String sql = "INSERT INTO showtime (id) values (?)";
        jdbcTemplate.update(sql, entity);
    }

    @Override
    public void update(ShowTime entity) {
        String sql = "UPDATE showtime SET id = ? WHERE id = ?";
        jdbcTemplate.update(sql,entity);

    }

    @Override
    public void delete(int entity) {
        String sql = "DELETE from showtime WHERE id = ?";
        jdbcTemplate.update(sql,entity);

    }

    @Override
    public ShowTime getById(int id) {
        return null;
    }

    @Override
    public List getAll() {
        return List.of();
    }
}

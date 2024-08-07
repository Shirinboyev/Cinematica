package uz.pdp.daos.roomsDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.daos.BaseDao;
import uz.pdp.model.Rooms;

import java.util.List;

@Component
public class RoomDao implements BaseDao<Rooms> {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Rooms> rowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int capacity = rs.getInt("capacity");
        return new Rooms(id, name, capacity);
    };

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Rooms entity) {
        String sql = "INSERT INTO rooms (name, capacity) VALUES (?, ?)";
        jdbcTemplate.update(sql, entity.getName(), entity.getCapacity());
    }

    @Override
    public void update(Rooms entity) {
        String sql = "UPDATE rooms SET name = ?, capacity = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getName(), entity.getCapacity(), entity.getId());

    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Rooms getById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Rooms> getAll() {
        String sql = "select * from rooms";
        return jdbcTemplate.query(sql, rowMapper);
    }
}

package uz.pdp.daos.screenDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.daos.BaseDao;
import uz.pdp.model.Screens;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ScreenDao implements BaseDao<Screens> {

    private final JdbcTemplate jdbcTemplate;

    public ScreenDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final RowMapper<Screens> rowMapper = (rs,rowMapper) -> mapScreen(rs);
        private Screens mapScreen(ResultSet rs) throws SQLException {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            Integer roomId = rs.getInt("room_id");
            return Screens.builder()
                    .id(id)
                    .name(name)
                    .roomId(roomId)
                    .build();
        }



    @Override
    public void save(Screens entity) {
        String sql = "INSERT INTO screens (name, room_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, entity.getName(), entity.getRoomId());
    }

    @Override
    public void update(Screens entity) {
        String sql = "UPDATE screens SET name = ?, room_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getName(), entity.getRoomId(), entity.getId());
    }

    @Override
    public void delete(int entity) {
        String sql = "DELETE FROM screens WHERE id = ?";
        jdbcTemplate.update(sql, entity);
    }

    @Override
    public Screens getById(int id) {
        String sql = "SELECT * FROM screens WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);

    }

    @Override
    public List<Screens> getAll() {
            String sql = "SELECT * FROM screens";
            return jdbcTemplate.query(sql, rowMapper);
    }
}

package uz.pdp.daos.ticketDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.daos.BaseDao;
import uz.pdp.model.Tickets;

import java.sql.Timestamp;
import java.util.List;

@Component
public class TicketDao implements BaseDao<Tickets> {

    private final JdbcTemplate jdbcTemplate;

    public TicketDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Tickets> rowMapper = (rs, rowNum) -> {
        int id = rs.getInt("id");
        Timestamp time = rs.getTimestamp("time");
        float price = rs.getFloat("price");
        int seatNumber = rs.getInt("seat_number");
        return Tickets.builder()
                .id(id)
                .price(price)
                .time(time)
                .seatNumber(seatNumber)
                .build();
    };

    @Override
    public void save(Tickets entity) {
        String sql = "INSERT INTO tickets (time, price, seat_number) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, entity.getTime(), entity.getPrice(), entity.getSeatNumber());
    }

    @Override
    public void update(Tickets entity) {
        String sql = "UPDATE tickets SET time = ?, price = ?, seat_number = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getTime(), entity.getPrice(), entity.getSeatNumber(), entity.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM tickets WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Tickets getById(int id) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Tickets> getAll() {
        String sql = "SELECT * FROM tickets";
        return jdbcTemplate.query(sql, rowMapper);
    }
}

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
        int userId = rs.getInt("user_id");
        int showtimeId = rs.getInt("showtime_id");
        Timestamp time = rs.getTimestamp("time");
        double price = rs.getDouble("price");
        int seatNumber = rs.getInt("seat_number");
        return Tickets.builder()
                .id(id)
                .userId(userId)
                .showtimeId(showtimeId)
                .time(time)
                .price(price)
                .seatNumber(seatNumber)
                .build();
    };

    @Override
    public void save(Tickets entity) {
        String sql = "INSERT INTO tickets (user_id, showtime_id, time, price, seat_number) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, entity.getUserId(), entity.getShowtimeId(), entity.getTime(), entity.getPrice(), entity.getSeatNumber());
    }

    @Override
    public void update(Tickets entity) {
        String sql = "UPDATE tickets SET user_id = ?, showtime_id = ?, time = ?, price = ?, seat_number = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getUserId(), entity.getShowtimeId(), entity.getTime(), entity.getPrice(), entity.getSeatNumber(), entity.getId());
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
    public List<Integer> getBookedSeats(int showTimeId) {
        String sql = "SELECT seat_number FROM tickets WHERE showtime_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{showTimeId}, Integer.class);
    }
}

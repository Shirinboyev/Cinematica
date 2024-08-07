package uz.pdp.daos.userDao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uz.pdp.daos.BaseDao;
import uz.pdp.enums.userState.UserState;
import uz.pdp.model.AuthUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserDao implements BaseDao<AuthUser> {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<AuthUser> rowMapper = (rs, rowNum) -> mapUser(rs);

    private AuthUser mapUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String fullName = rs.getString("full_name");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString(UserState.USER.ordinal());

        LocalDateTime createdAt = null;
        try {
            createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        } catch (SQLException e) {

        }

        return AuthUser.builder()
                .id(id)
                .fullName(fullName)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .createDate(createdAt)
                .build();
    }

    @Override
    public void save(AuthUser entity) {
        String sql = "INSERT INTO users (full_name, username, email, password) VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(sql, entity.getFullName(), entity.getUsername(), entity.getEmail(), entity.getPassword());
    }

    @Override
    public void update(AuthUser entity) {
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public AuthUser getById(int id) {
        String query = "SELECT * FROM users WHERE id=?;";
        List<AuthUser> users = jdbcTemplate.query(query, rowMapper, id);
        return users.isEmpty() ? null : users.getFirst();
    }

    @Override
    public List<AuthUser> getAll() {
        String query = "SELECT * FROM users;";
        return jdbcTemplate.query(query, rowMapper);
    }

    public AuthUser getByUsername(String username) {
        String query = "SELECT * FROM users WHERE username=?;";
        List<AuthUser> users = jdbcTemplate.query(query, rowMapper, username);
        return users.isEmpty() ? null : users.getFirst();
    }
}

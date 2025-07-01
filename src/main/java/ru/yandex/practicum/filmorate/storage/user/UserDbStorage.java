package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setLogin(rs.getString("login"));
        u.setName(rs.getString("name"));
        u.setBirthday(rs.getDate("birthday").toLocalDate());
        return u;
    };

    @Override
    public User add(User user) {
        String sql = """
            INSERT INTO users (email, login, name, birthday)
            VALUES (?, ?, ?, ?)
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return getById(user.getId());
    }

    @Override
    public User update(User user) {
        String sql = """
            UPDATE users
               SET email    = ?,
                   login    = ?,
                   name     = ?,
                   birthday = ?
             WHERE id = ?
        """;
        int updated = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );
        if (updated == 0) {
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден");
        }
        return getById(user.getId());
    }

    @Override
    public void delete(int userId) {
        jdbcTemplate.update("DELETE FROM users WHERE id= ?", userId);
    }

    @Override
    public User getById(int userId) {
        String sql = "SELECT * FROM users WHERE id= ?";
        return jdbcTemplate.queryForObject(sql, userMapper, userId);
    }

    @Override
    public Collection<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users", userMapper);
    }

    /*@Override
    public void addFriend(int userId, int friendId) {
        String sql = """
            INSERT INTO friendships (user_id, friend_id, status)
            VALUES (?, ?, 'PENDING')
        """;
        jdbcTemplate.update(sql, userId, friendId);
    }*/

    @Override
    public void addFriend(int userId, int friendId) {
        // проверяем: есть ли заявка от friendId к userId?
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM friendships WHERE id= ? AND friend_id = ?",
                Integer.class,
                friendId, userId
        );

        if (cnt != null && cnt > 0) {
            // подтверждаем обе записи сразу
            String sql = """
            UPDATE friendships
               SET status = 'CONFIRMED'
             WHERE (id= ? AND friend_id = ?)
                OR (id= ? AND friend_id = ?)
            """;
            jdbcTemplate.update(sql, userId, friendId, friendId, userId);
            log.info("Пользователи {} и {} теперь друзья (confirmed)", userId, friendId);
        } else {
            // создаём заявку PENDING (если уже есть, можно игнорировать дубликат)
            String sql = """
            MERGE INTO friendships (user_id, friend_id, status)
            KEY (user_id, friend_id)
            VALUES (?, ?, 'PENDING')
            """;
            jdbcTemplate.update(sql, userId, friendId);
            log.info("Пользователь {} отправил заявку в друзья к {} (pending)", userId, friendId);
        }
    }

    /*@Override
    public void removeFriend(int userId, int friendId) {
        jdbcTemplate.update(
                "DELETE FROM friendships WHERE id= ? AND friend_id = ?",
                userId, friendId
        );
    }*/

    @Override
    public void removeFriend(int userId, int friendId) {
        // сначала удаляем любую заявку или дружбу в одну сторону
        jdbcTemplate.update(
                "DELETE FROM friendships WHERE id= ? AND friend_id = ?",
                userId, friendId
        );
        // если они уже были confirmed, сломаем и обратную связь
        jdbcTemplate.update(
                "DELETE FROM friendships WHERE id= ? AND friend_id = ?",
                friendId, userId
        );
        log.info("Пользователь {} разорвал дружбу или отозвал заявку к {}", userId, friendId);
    }

    @Override
    public Collection<User> getFriends(int userId) {
        String sql = """
            SELECT u.* 
              FROM users u
              JOIN friendships f 
                ON u.id= f.friend_id
             WHERE f.id= ? AND f.status = 'CONFIRMED'
        """;
        return jdbcTemplate.query(sql, userMapper, userId);
    }

    @Override
    public Collection<User> getCommonFriends(int userId, int otherId) {
        String sql = """
            SELECT u.*
              FROM users u
              JOIN friendships f1 
                ON u.id= f1.friend_id
               AND f1.id= ?
               AND f1.status = 'CONFIRMED'
              JOIN friendships f2 
                ON u.id= f2.friend_id
               AND f2.id= ?
               AND f2.status = 'CONFIRMED'
        """;
        return jdbcTemplate.query(sql, userMapper, userId, otherId);
    }
}

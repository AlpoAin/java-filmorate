package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import java.util.Collection;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbc;

    private final RowMapper<Genre> mapper = (rs, rn) ->
            new Genre(rs.getInt("id"), rs.getString("name"));

    public GenreDbStorage(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    public Collection<Genre> findAll() {
        return jdbc.query("SELECT id, name FROM genres", mapper);
    }
    @Override
    public Genre findById(int id) {
        return jdbc.queryForObject(
                "SELECT id, name FROM genres WHERE id = ?", mapper, id
        );
    }
}

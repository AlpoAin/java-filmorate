package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.mpa.MpaRating;
import java.util.Collection;

@Repository
public class MpaRatingDbStorage implements MpaRatingStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<MpaRating> mapper = (rs, rn) ->
            new MpaRating(rs.getString("code"), rs.getString("description"));
    public MpaRatingDbStorage(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    @Override
    public Collection<MpaRating> findAll() {
        return jdbc.query("SELECT code, description FROM mpa_ratings", mapper);
    }
    @Override
    public MpaRating findByCode(String code) {
        return jdbc.queryForObject(
                "SELECT code, description FROM mpa_ratings WHERE code = ?", mapper, code
        );
    }
}

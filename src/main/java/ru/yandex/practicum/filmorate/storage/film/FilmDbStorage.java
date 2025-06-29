package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Маппер базовых полей + mpa_rating.
     * Жанры подтягиваем отдельно в методе fillGenres().
     */
    private final RowMapper<Film> filmMapper = (rs, rowNum) -> {
        Film f = new Film();
        f.setId(rs.getInt("id"));
        f.setName(rs.getString("name"));
        f.setDescription(rs.getString("description"));
        f.setReleaseDate(rs.getDate("release_date").toLocalDate());
        f.setDuration(rs.getLong("duration"));
        f.setMpaRating(rs.getString("mpa_rating"));
        return f;
    };

    @Override
    public Film add(Film film) {
        String sql = """
            INSERT INTO films (name, description, release_date, duration, mpa_rating)
            VALUES (?, ?, ?, ?, ?)
            """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setString(5, film.getMpaRating());
            return ps;
        }, keyHolder);

        int id = keyHolder.getKey().intValue();
        film.setId(id);
        updateFilmGenres(film);
        return getById(id);
    }

    @Override
    public Film update(Film film) {
        String sql = """
            UPDATE films
               SET name         = ?,
                   description  = ?,
                   release_date = ?,
                   duration     = ?,
                   mpa_rating   = ?
             WHERE id = ?
            """;
        int updated = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpaRating(),
                film.getId()
        );
        if (updated == 0) {
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        updateFilmGenres(film);
        return getById(film.getId());
    }

    private void updateFilmGenres(Film film) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
        for (Integer genreId : film.getGenreIds()) {
            jdbcTemplate.update(
                    "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                    film.getId(), genreId
            );
        }
    }

    @Override
    public void delete(int filmId) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", filmId);
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ?", filmId);
        jdbcTemplate.update("DELETE FROM films WHERE id = ?", filmId);
    }

    @Override
    public Film getById(int filmId) {
        String sql = """
            SELECT id, name, description, release_date, duration, mpa_rating
              FROM films
             WHERE id = ?
            """;
        Film film = jdbcTemplate.queryForObject(sql, filmMapper, filmId);
        fillGenres(film);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        String sql = """
            SELECT id, name, description, release_date, duration, mpa_rating
              FROM films
            """;
        List<Film> films = jdbcTemplate.query(sql, filmMapper);
        films.forEach(this::fillGenres);
        return films;
    }

    @Override
    public void addLike(int filmId, int userId) {
        jdbcTemplate.update(
                "INSERT INTO likes (film_id, user_id) VALUES (?, ?)",
                filmId, userId
        );
    }

    @Override
    public void removeLike(int filmId, int userId) {
        jdbcTemplate.update(
                "DELETE FROM likes WHERE film_id = ? AND user_id = ?",
                filmId, userId
        );
    }

    @Override
    public Collection<Film> getPopular(int count) {
        String sql = """
            SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_rating
              FROM films f
              LEFT JOIN likes l ON f.id = l.film_id
             GROUP BY f.id
             ORDER BY COUNT(l.user_id) DESC
             LIMIT ?
            """;
        List<Film> films = jdbcTemplate.query(sql, filmMapper, count);
        films.forEach(this::fillGenres);
        return films;
    }

    /** Подтянуть список жанров для одного фильма */
    private void fillGenres(Film film) {
        List<Integer> genreIds = jdbcTemplate.queryForList(
                "SELECT genre_id FROM film_genres WHERE film_id = ?", Integer.class, film.getId()
        );
        film.getGenreIds().clear();
        film.getGenreIds().addAll(genreIds);
    }
}

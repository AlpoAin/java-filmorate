package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import({
        ru.yandex.practicum.filmorate.storage.film.FilmDbStorage.class,
        ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage.class,
        ru.yandex.practicum.filmorate.storage.mpa.MpaRatingDbStorage.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageIntegrationTest {

    private final FilmDbStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaRatingStorage mpaRatingStorage;

    @Test
    void shouldSaveAndLoadFilmWithGenresAndMpa() {
        // создаём тестовый фильм
        Film film = new Film();
        film.setName("Integration Test");
        film.setDescription("Описание для интеграционного теста");
        film.setReleaseDate(LocalDate.of(2020, 5, 20));
        film.setDuration(123);
        // используем существующие в data.sql жанры и mpa
        film.setGenreIds(Set.of(1, 2));     // «Комедия», «Драма»
        film.setMpaRating("PG-13");

        // сохраняем
        Film saved = filmStorage.add(film);

        // читаем обратно
        Film loaded = filmStorage.getById(saved.getId());

        assertNotNull(loaded);
        assertEquals("Integration Test", loaded.getName());
        assertEquals("PG-13", loaded.getMpaRating());
        assertEquals(Set.of(1, 2), loaded.getGenreIds());
    }
}

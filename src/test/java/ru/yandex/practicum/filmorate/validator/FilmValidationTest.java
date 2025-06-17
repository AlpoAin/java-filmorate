package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {
    private final InMemoryFilmStorage storage = new InMemoryFilmStorage();

    @Test
    void rejectTooOldReleaseDate() {
        Film film = new Film();
        // заполните все обязательные поля
        film.setName("Test");
        film.setDescription("Desc");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> storage.add(film)
        );
        assertTrue(ex.getMessage().contains("не может быть раньше"));
    }

    @Test
    void acceptValidFilm() {
        Film film = new Film();
        film.setName("Valid");
        film.setDescription("Desc");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        Film added = storage.add(film);
        assertEquals(1, added.getId());
    }
}

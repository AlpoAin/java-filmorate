package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

/**
 * Хранилище фильмов в памяти.
 */
@Component
@Slf4j
public class InMemoryFilmStorage implements Storage<Film> {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @Override
    public Film add(Film film) {
        validate(film);
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {} (id={})", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id=" + film.getId() + " не найден");
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Обновлён фильм: id={}", film.getId());
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    private void validate(Film f) {
        LocalDate firstRelease = LocalDate.of(1895, 12, 28);
        if (f.getReleaseDate().isBefore(firstRelease)) {
            throw new ValidationException(
                    "Дата релиза не может быть раньше " + firstRelease);
        }
    }
}

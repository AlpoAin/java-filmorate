package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmStorage {
    Film add(Film film);
    Film update(Film film);
    void delete(int filmId);
    Film getById(int filmId);
    Collection<Film> getAll();
    void addLike(int filmId, int userId);
    void removeLike(int filmId, int userId);
    Collection<Film> getPopular(int count);
}

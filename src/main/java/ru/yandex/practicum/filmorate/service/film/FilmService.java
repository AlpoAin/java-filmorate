package ru.yandex.practicum.filmorate.service.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
public class FilmService {
    private final FilmStorage storage;

    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public Film add(Film film) {
        return storage.add(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public void delete(int filmId) {
        storage.delete(filmId);
    }

    public Film getById(int filmId) {
        return storage.getById(filmId);
    }

    public Collection<Film> getAll() {
        return storage.getAll();
    }

    // лайки
    public void addLike(int filmId, int userId) {
        storage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        storage.removeLike(filmId, userId);
    }

    public Collection<Film> getPopular(int count) {
        return storage.getPopular(count);
    }
}

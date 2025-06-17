package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    // хранение самих фильмов
    private final Map<Integer, Film> films = new HashMap<>();
    // для каждого фильма — множество id пользователей, поставивших лайк
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();
    private int nextId = 1;

    @Override
    public Film add(Film film) {
        FilmValidator.validate(film);
        film.setId(nextId++);
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        log.info("Добавлен фильм: {} (id={})", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        FilmValidator.validate(film);
        films.put(film.getId(), film);
        log.info("Обновлён фильм: id={}", film.getId());
        return film;
    }

    @Override
    public void delete(int filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }
        films.remove(filmId);
        likes.remove(filmId);
        log.info("Удалён фильм: id={}", filmId);
    }

    @Override
    public Film getById(int filmId) {
        Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id=" + filmId + " не найден");
        }
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLike(int filmId, int userId) {
        // проверка, что фильм существует
        getById(filmId);
        likes.computeIfAbsent(filmId, id -> new HashSet<>()).add(userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        getById(filmId);
        Set<Integer> userLikes = likes.get(filmId);
        if (userLikes != null) {
            userLikes.remove(userId);
            log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
        }
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return films.values().stream()
                .sorted((f1, f2) -> {
                    int s1 = likes.getOrDefault(f1.getId(), Collections.emptySet()).size();
                    int s2 = likes.getOrDefault(f2.getId(), Collections.emptySet()).size();
                    return Integer.compare(s2, s1);
                })
                .limit(count)
                .toList();
    }
}

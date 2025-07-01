package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    /** Возвращает все жанры */
    public Collection<Genre> getAll() {
        return genreStorage.findAll();
    }

    /** Возвращает жанр по ID, бросает исключение, если не найден */
    public Genre getById(int id) {
        return genreStorage.findById(id);
    }
}

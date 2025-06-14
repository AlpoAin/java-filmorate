package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import jakarta.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final InMemoryFilmStorage storage;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return storage.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return storage.update(film);
    }

    @GetMapping
    public Collection<Film> getAll() {
        return storage.getAll();
    }
}

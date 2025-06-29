package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    /** GET /genres */
    @GetMapping
    public Collection<Genre> getAll() {
        return genreService.getAll();
    }

    /** GET /genres/{id} */
    @GetMapping("/{id}")
    public Genre getById(@PathVariable int id) {
        return genreService.getById(id);
    }
}

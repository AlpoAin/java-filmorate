package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.mpa.MpaRating;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    /** GET /mpa */
    @GetMapping
    public Collection<MpaRating> getAll() {
        return mpaService.getAll();
    }

    /** GET /mpa/{code} */
    @GetMapping("/{code}")
    public MpaRating getByCode(@PathVariable String code) {
        return mpaService.getByCode(code);
    }
}

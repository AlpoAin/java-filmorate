package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import jakarta.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final InMemoryUserStorage storage;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return storage.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return storage.update(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        return storage.getAll();
    }
}

package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

import java.util.Set;
import java.util.HashSet;


/**
 * Модель фильма.
 */
@Data
public class Film {
    private int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза обязательна")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной")
    private long duration;

    @NotBlank(message = "MPA-рейтинг не может быть пустым")
    private String mpaRating;

    /** Список id жанров */
    private Set<@Positive Integer> genreIds;

    // Явно пишем конструктор, инициализируем коллекцию
    public Film() {
        this.genreIds = new HashSet<>();
    }

    /**
     * Перегруженный сеттер для genreIds:
     * если Jackson или кто-то передаст null — мы всё равно оставим пустой Set*/
    public void setGenreIds(Set<Integer> genreIds) {
        this.genreIds = (genreIds != null) ? genreIds : new HashSet<>();
    }
}

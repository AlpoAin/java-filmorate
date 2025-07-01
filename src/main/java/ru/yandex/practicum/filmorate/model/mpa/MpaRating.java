package ru.yandex.practicum.filmorate.model.mpa;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MpaRating {
    private String code;        // например "PG-13"
    private String description;
}

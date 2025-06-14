package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Модель пользователя.
 */
@Data
@NoArgsConstructor
public class User {
    private int id;

    @Email(message = "Нужно указать корректный e-mail")
    @NotBlank(message = "E-mail не может быть пустым")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы")
    private String login;

    /** Если name пуст, в сервисе подставим login */
    private String name;

    @NotNull(message = "Дата рождения обязательна")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}

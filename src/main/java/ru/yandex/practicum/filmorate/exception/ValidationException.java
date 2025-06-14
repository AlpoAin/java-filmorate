package ru.yandex.practicum.filmorate.exception;

/**
 * Бросается при ошибках валидации пользовательских данных.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

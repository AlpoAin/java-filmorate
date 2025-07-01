package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;


/**
 * Глобальная обработка ошибок валидации и прочих исключений.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Наше кастомное исключение валидации
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidation(ValidationException e) {
        log.warn("Ошибка валидации: {}", e.getMessage());
        return e.getMessage();
    }

    // Исключения, брошенные из @Valid при привязке тела запроса
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBind(MethodArgumentNotValidException e) {
        FieldError err = e.getBindingResult().getFieldError();
        String msg = (err != null)
                ? err.getField() + ": " + err.getDefaultMessage()
                : "Неверный формат запроса";
        log.warn("Ошибка привязки параметров: {}", msg);
        return msg;
    }

    // Не найден объект
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NotFoundException e) {
        log.warn("Не найдено: {}", e.getMessage());
        return e.getMessage();
    }

}

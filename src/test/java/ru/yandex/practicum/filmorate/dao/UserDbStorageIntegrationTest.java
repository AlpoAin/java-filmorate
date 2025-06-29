package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(UserDbStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageIntegrationTest {

    private final UserDbStorage userStorage;

    @Test
    void shouldSaveAndLoadUser() {
        // готовим тестового пользователя
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        // сохраняем
        User saved = userStorage.add(user);

        // читаем обратно
        User loaded = userStorage.getById(saved.getId());

        assertNotNull(loaded);
        assertEquals("test@example.com", loaded.getEmail());
        assertEquals("testuser", loaded.getLogin());
        assertEquals(LocalDate.of(1990, 1, 1), loaded.getBirthday());
    }
}

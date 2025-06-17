package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    // хранение пользователей
    private final Map<Integer, User> users = new HashMap<>();
    // для каждого пользователя — множество id его друзей
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();
    private int nextId = 1;

    @Override
    public User add(User user) {
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(nextId++);
        users.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>());
        log.info("Добавлен пользователь: {} (id={})", user.getLogin(), user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден");
        }
        UserValidator.validate(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Обновлён пользователь: id={}", user.getId());
        return user;
    }

    @Override
    public void delete(int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        users.remove(userId);
        friends.remove(userId);
        // удаляем из чужих списков
        for (Set<Integer> set : friends.values()) {
            set.remove(userId);
        }
        log.info("Удалён пользователь: id={}", userId);
    }

    @Override
    public User getById(int userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        friends.get(userId).add(friendId);
        friends.get(friendId).add(userId);
        log.info("Пользователь {} и {} теперь друзья", userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        getById(userId);
        getById(friendId);
        friends.getOrDefault(userId, Collections.emptySet()).remove(friendId);
        friends.getOrDefault(friendId, Collections.emptySet()).remove(userId);
        log.info("Пользователь {} и {} разорвали дружбу", userId, friendId);
    }

    @Override
    public Collection<User> getFriends(int userId) {
        getById(userId);
        return friends.getOrDefault(userId, Collections.emptySet()).stream()
                .map(this::getById)
                .toList();
    }

    @Override
    public Collection<User> getCommonFriends(int userId, int otherId) {
        getById(userId);
        getById(otherId);
        Set<Integer> set1 = friends.getOrDefault(userId, Collections.emptySet());
        Set<Integer> set2 = friends.getOrDefault(otherId, Collections.emptySet());
        return set1.stream()
                .filter(set2::contains)
                .map(this::getById)
                .toList();
    }
}

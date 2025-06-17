package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
public class UserService {
    private final UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User add(User user) {
        return storage.add(user);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public void delete(int userId) {
        storage.delete(userId);
    }

    public User getById(int userId) {
        return storage.getById(userId);
    }

    public Collection<User> getAll() {
        return storage.getAll();
    }

    // друзья
    public void addFriend(int userId, int friendId) {
        storage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        storage.removeFriend(userId, friendId);
    }

    public Collection<User> getFriends(int userId) {
        return storage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(int userId, int otherId) {
        return storage.getCommonFriends(userId, otherId);
    }
}

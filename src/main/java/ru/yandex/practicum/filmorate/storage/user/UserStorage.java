package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserStorage {
    User add(User user);
    User update(User user);
    void delete(int userId);
    User getById(int userId);
    Collection<User> getAll();
    void addFriend(int userId, int friendId);
    void removeFriend(int userId, int friendId);
    Collection<User> getFriends(int userId);
    Collection<User> getCommonFriends(int userId, int otherId);
}

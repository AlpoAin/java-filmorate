package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

/**
 * Обобщённый интерфейс для in-memory хранилищ.
 */
public interface Storage<T> {
    T add(T item);
    T update(T item);
    Collection<T> getAll();
}

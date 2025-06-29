package ru.yandex.practicum.filmorate.storage.mpa;
import ru.yandex.practicum.filmorate.model.mpa.MpaRating;
import java.util.Collection;

public interface MpaRatingStorage {
    Collection<MpaRating> findAll();
    MpaRating findByCode(String code);
}

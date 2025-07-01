package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.mpa.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRatingStorage mpaRatingStorage;

    /** Возвращает все рейтинги MPA */
    public Collection<MpaRating> getAll() {
        return mpaRatingStorage.findAll();
    }

    /** Возвращает рейтинг по коду (например "PG-13") */
    public MpaRating getByCode(String code) {
        return mpaRatingStorage.findByCode(code);
    }
}

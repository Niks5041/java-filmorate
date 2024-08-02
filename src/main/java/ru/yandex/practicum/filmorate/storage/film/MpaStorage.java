package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.util.Collection;

public interface MpaStorage {
    Collection<Mpa> getAllRatings();

    Mpa findRatingById(Integer id);
}

package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Like;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film addNewFilm(Film film);

    Film updateFilm(Film updatedFilm);

    Film findFilmById(Integer id);

    void addLike(Like like);

    void deleteLike(Integer filmId, Integer userId);
}



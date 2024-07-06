package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film addNewFilm(Film film);

    Film updateFilm(Film updatedFilm);

    Film findFilmById(Long id);
}
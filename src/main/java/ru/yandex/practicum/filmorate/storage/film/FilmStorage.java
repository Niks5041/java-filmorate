package ru.yandex.practicum.filmorate.storage.film;

import java.util.Collection;

import ru.yandex.practicum.filmorate.model.films.Film;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Collection<Film> getAllPopFilms();

    Film addNewFilm(Film film);

    Film updateFilm(Film updatedFilm);

    Film findFilmById(Integer id);

    void deleteFilmById(Integer id);

    Collection<Film> findFilmsByDirector(String query);

    Collection<Film> findFilmsByTitle(String query);
}



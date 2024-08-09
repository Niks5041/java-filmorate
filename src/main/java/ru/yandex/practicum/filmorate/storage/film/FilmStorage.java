package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.films.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Collection<Film> getAllPopFilms();

    Film addNewFilm(Film film);

    Film updateFilm(Film updatedFilm);

    Film findFilmById(Integer id);

    void deleteFilmById(Integer id);

    List<Film> getAllFilmsByDirectorAndLikes(Integer id);

    List<Film> getAllFilmsByDirectorAndYear(Integer id);
}



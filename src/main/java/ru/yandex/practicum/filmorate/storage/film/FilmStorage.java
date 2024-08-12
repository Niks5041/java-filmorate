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

    Collection<Film> findFilmsByDirector(String query);

    Collection<Film> findFilmsByTitle(String query);

    Collection<Film> findFilmsByTitleAndDirector(String query);

    Collection<Film> getCommonFilms(Integer userId, Integer friendId);
}



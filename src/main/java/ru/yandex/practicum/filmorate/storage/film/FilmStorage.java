package ru.yandex.practicum.filmorate.storage.film;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.model.films.Film;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Collection<Film> getAllPopFilms();

    Film addNewFilm(Film film);

    Film updateFilm(Film updatedFilm);

    Film findFilmById(Integer id);

    void deleteFilmById(Integer id);

    List<Film> getAllFilmsByDirectorAndLikes(Integer id);

    List<Film> getAllFilmsByDirectorAndYear(Integer id);

    List<Film> findFilmsByDirector(String query);

    List<Film> findFilmsByTitle(String query);

    List<Film> findFilmsByTitleAndDirector(String query);

    Collection<Film> getCommonFilms(Integer userId, Integer friendId);
}



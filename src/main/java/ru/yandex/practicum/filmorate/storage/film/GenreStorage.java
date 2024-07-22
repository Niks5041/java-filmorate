package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.films.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    Set<Genre> getAllGenres();
    Genre findGenreById(Integer id);
    List<Genre> getGenreByFilmId(Integer id);
}

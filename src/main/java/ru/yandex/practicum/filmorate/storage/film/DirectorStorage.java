package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.films.Director;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface DirectorStorage {
    Collection<Director> getAllDirectors();

    Director findDirectorById(Integer id);

    Director addNewDirector(Director director);

    Director updateDirector(Director updatedDirector);

    void deleteDirectorById(Integer id);

    void addFilmToDirector(int filmId, Set<Integer> directorIds);

    List<Director> getDirectorByFilmId(Integer id);
}

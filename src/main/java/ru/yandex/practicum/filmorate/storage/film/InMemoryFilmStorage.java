package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private long generatorId = 0;
    private final Map<Long, Film> films = new HashMap<>();

    public Film findFilmById(Long filmId) {
        log.info("Получен запрос на фильм по ID {}", filmId);
        Film foundFilm = films.values().stream()
                .filter(filmSaved -> filmId == filmSaved.getId())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден"));

        log.info("Получен фильм по ID {}: {}", filmId, foundFilm);
        return foundFilm;
    }

    public Collection<Film> getAllFilms() {
        Collection<Film> filmsList = films.values();
        log.info("Получен список всех фильмов{}", filmsList);
        return filmsList;
    }

    public Film addNewFilm(Film film) {
        film.setId(++generatorId);
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен: {}", film);
        return film;
    }

    public Film updateFilm(Film updatedFilm) {
        Film oldFilm = films.get(updatedFilm.getId());
        if (oldFilm == null) {
            log.info("Фильм c ID " + updatedFilm.getId() + " не найден!");
            throw new NotFoundException("Фильм с ID " + updatedFilm.getId() + " не найден!");
        }
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Информация о фильме успешно обновлена: {}", updatedFilm);
        return updatedFilm;
    }
}


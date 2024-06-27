package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.time.Month.DECEMBER;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private long generatorId = 0;
    private final Map<Long, Film> films = new HashMap<>();

    public Film findFilmById(Long filmId) {
        log.info("Получен запрос на фильм по ID {}",filmId);
        log.info("Получен фильм по ID {}",filmId);
        return films.values().stream()
                .filter(filmSaved -> filmId == filmSaved.getId())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с ID " + filmId + " не найден"));
    }

    public Collection<Film> getAllFilms() {
        log.info("Получен список всех фильмов{}", films.values());
        return films.values();
    }

    public Film addNewFilm(Film film) {
        checkValid(film);
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
        checkValid(updatedFilm);
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Информация о фильме успешно обновлена: {}", updatedFilm);
        return updatedFilm;
    }

    private void checkValid(Film film) {
        if (film.getName().isEmpty() || film.getName() == null) {
            log.info("Название фильма не может быть пустым! Фильм: {}", film);
            throw new ValidationException("Название фильма не может быть пустым!");
        }
        if (film.getDescription().length() > 200) {
            log.info("Превышена максимальная длина описания фильма (200 символов). Фильм: {}", film);
            throw new ValidationException("Превышена максимальная длина описания фильма (200 символов)");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 28))) {
            log.info("Дата релиза фильма не может быть раньше 28 декабря 1895 года. Фильм: {}", film);
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.info("Продолжительность фильма не может быть отрицательным числом. Фильм: {}", film);
            throw new ValidationException("Продолжительность фильма не может быть отрицательным числом");
        }
    }
}

package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static java.time.Month.DECEMBER;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private long generatorId = 0;
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Запрос на получение списка всех фильмов");
        return films.values();
    }

    @PostMapping
    public ResponseEntity<Film> addNewFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление нового фильма: {}", film);
        checkValid(film);
        film.setId(++generatorId);
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен: {}", film);
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film updatedFilm) {
        log.info("Получен запрос на обновление информации о фильме с ID: {}", updatedFilm.getId());
        Film oldFilm = films.get(updatedFilm.getId());
        if (oldFilm == null) {
            log.info("Фильм c ID " + updatedFilm.getId() + " не найден!");
            throw new NotFoundException("Фильм с ID " + updatedFilm.getId() + " не найден!");
        }
        checkValid(updatedFilm);
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Информация о фильме успешно обновлена: {}", updatedFilm);
        return ResponseEntity.ok().body(updatedFilm);
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
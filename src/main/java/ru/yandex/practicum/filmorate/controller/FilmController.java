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

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public ResponseEntity addNewFilm(@Valid @RequestBody Film film) {
        if (film.getName().isEmpty() || film.getName() == null) {
            log.info("Название не может быть пустым!");
            throw new NotFoundException("Ошибка!");
        }
        if (film.getDescription().length() > 200) {
            log.info("Максимальная длина описания — 200 символов");
            throw new ValidationException("Ошибка!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 28))) {
            log.info("Дата релиза не должна быть раньше 28 декабря 1895 года");
            throw new ValidationException("Ошибка!");
        }

        if (film.getDuration().getSeconds() < 0) {
            log.info("Продолжительность фильма не может быть отрицательным числом");
            throw new ValidationException("Ошибка!");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);

        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film updatedFilm) {
        Film oldFilm = films.get(updatedFilm.getId());
        if (oldFilm == null) {
            log.info("Фильм не найден!");
            throw new NotFoundException("Ошибка!");
        }
        if (updatedFilm.getName() != null) {
            oldFilm.setName(updatedFilm.getName());
        }
        if (updatedFilm.getDescription() != null) {
            oldFilm.setDescription(updatedFilm.getDescription());
        }
        if (updatedFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
        }
        if (updatedFilm.getDuration() != null) {
            oldFilm.setDuration(updatedFilm.getDuration());
        }

        return ResponseEntity.ok().body(oldFilm);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}



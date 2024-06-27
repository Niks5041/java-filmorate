package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос на получение списка всех фильмов");
        return filmStorage.getAllFilms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addNewFilm(@RequestBody Film film) {
        log.info("Получен запрос на добавление нового фильма: {}", film);
        return filmStorage.addNewFilm(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody Film updatedFilm) {
        log.info("Получен запрос на обновление информации о фильме с ID: {}", updatedFilm.getId());
        return filmStorage.updateFilm(updatedFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Получен запрос на лайк пользователя с ID {}" +
                " к фильму с ID {}", userId, filmId);
        filmService.addNewLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Получен запрос на удаление лайка пользователя с ID {}" +
                " к фильму с ID {}", userId, filmId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getListOfPopularFilms(@RequestParam int count) {
        log.info("Получен запрос на получение {} популярных фильмов", count);
        return filmService.getListOfPopularFilms(count);
    }
}

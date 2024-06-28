package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms() {
        log.info("Пришел GET запрос /films");
        Collection<Film> films = filmService.getAllFilms();
        log.info("Отправлен ответ GET /films c телом: {}", films);
        return films;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addNewFilm(@RequestBody Film film) {
        log.info("Пришел POST запрос /films с телом: {}", film);
        Film addedFilm = filmService.addNewFilm(film);
        log.info("Отправлен ответ POST /films: {}", addedFilm);
        return addedFilm;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody Film updatedFilm) {
        log.info("Пришел PUT запрос /films с телом: {}", updatedFilm);
        Film updated = filmService.updateFilm(updatedFilm);
        log.info("Отправлен ответ PUT /films: {}", updated);
        return updated;
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Пришел PUT запрос /films/{}/like/{}", filmId, userId);
        filmService.addNewLike(filmId, userId);
        log.info("Отправлен ответ PUT /films/{}/like/{}", filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Пришел DELETE запрос /films/{}/like/{}", filmId, userId);
        filmService.deleteLike(filmId, userId);
        log.info("Отправлен ответ DELETE /films/{}/like/{}", filmId, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getListOfPopularFilms(@RequestParam int count) {
        log.info("Пришел GET запрос /films/popular с параметром count={}", count);
        Collection<Film> popularFilms = filmService.getListOfPopularFilms(count);
        log.info("Отправлен ответ GET /films/popular: {}", popularFilms);
        return popularFilms;
    }
}

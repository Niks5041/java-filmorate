package ru.yandex.practicum.filmorate.controller.Film;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.dto.FilmDto;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
@Validated
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/{filmId}")
    public FilmDto getFilmById(@PathVariable Integer filmId) {
        log.info("Пришел GET запрос /films/{}", filmId);
        FilmDto film = filmService.getFilmById(filmId);
        log.info("Отправлен ответ GET /films/{} c телом: {}", filmId, film);
        return film;
    }

    @GetMapping
    public Collection<FilmDto> getAllFilms() {
        log.info("Пришел GET запрос /films");
        Collection<FilmDto> films = filmService.getAllFilms();
        log.info("Отправлен ответ GET /films c телом: {}", films);
        return films;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto addNewFilm(@RequestBody @Valid Film film) {
        log.info("Пришел POST запрос /films с телом: {}", film);
        FilmDto addedFilm = filmService.addNewFilm(film);
        log.info("Отправлен ответ POST /films: {}", addedFilm);
        return addedFilm;
    }

    @PutMapping
    public FilmDto updateFilm(@RequestBody @Valid Film updatedFilm) {
        log.info("Пришел PUT запрос /films с телом: {}", updatedFilm);
        FilmDto updated = filmService.updateFilm(updatedFilm);
        log.info("Отправлен ответ PUT /films: {}", updated);
        return updated;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("Пришел PUT запрос /films/{}/like/{}", filmId, userId);
        filmService.addNewLike(filmId, userId);
        log.info("Отправлен ответ PUT /films/{}/like/{}", filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        log.info("Пришел DELETE запрос /films/{}/like/{}", filmId, userId);
        filmService.deleteLike(filmId, userId);
        log.info("Отправлен ответ DELETE /films/{}/like/{}", filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getListOfPopularFilms(@Positive @RequestParam int count) {
        log.info("Пришел GET запрос /films/popular с параметром count={}", count);
        Collection<FilmDto> popularFilms = filmService.getListOfPopularFilms(count);
        log.info("Отправлен ответ GET /films/popular: {}", popularFilms);
        return popularFilms;
    }
}

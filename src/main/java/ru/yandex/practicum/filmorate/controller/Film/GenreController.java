package ru.yandex.practicum.filmorate.controller.Film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
@AllArgsConstructor
public class GenreController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Genre> getAllGenres() {
        log.info("Пришел GET запрос /genres");
        Collection<Genre> genres = filmService.getAllGenres();
        log.info("Отправлен ответ GET /films/genres c телом: {}", genres);
        return genres;
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        log.info("Пришел GET запрос /genres/{}", id);
        Genre genre = filmService.getGenreById(id);
        log.info("Отправлен ответ GET /films/genres/{} c телом: {}", id, genre);
        return genre;
    }
}

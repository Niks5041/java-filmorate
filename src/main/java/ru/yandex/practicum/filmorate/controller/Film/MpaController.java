package ru.yandex.practicum.filmorate.controller.Film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.films.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@AllArgsConstructor
public class MpaController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Mpa> getAllRatings() {
        log.info("Пришел GET запрос /mpa");
        Collection<Mpa> ratings = filmService.getAllRatings();
        log.info("Отправлен ответ GET /mpa c телом: {}", ratings);
        return ratings;
    }

    @GetMapping("/{id}")
    public Mpa getRatingById(@PathVariable Integer id) {
        log.info("Пришел GET запрос /mpa/{}", id);
        Mpa rating = filmService.getRatingById(id);
        log.info("Отправлен ответ GET /mpa/{} c телом: {}", id, rating);
        return rating;
    }
}

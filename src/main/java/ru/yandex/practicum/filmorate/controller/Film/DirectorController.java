package ru.yandex.practicum.filmorate.controller.Film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.films.Director;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/directors")
@Slf4j
@AllArgsConstructor
public class DirectorController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Director> getAllDirectors() {
        log.info("Пришел GET запрос /directors");
        Collection<Director> directors = filmService.getAllDirectors();
        log.info("Отправлен ответ GET /directors с телом: {}", directors);
        return directors;
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Integer id) {
        log.info("Пришел GET запрос /directors/{}", id);
        Director director = filmService.getDirectorById(id);
        log.info("Отправлен ответ GET /directors/{} с телом: {}", id, director);
        return director;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director createDirector(@RequestBody Director director) {
        log.info("Пришел POST запрос /directors с телом: {}", director);
        Director createdDirector = filmService.createDirector(director);
        log.info("Отправлен ответ POST /directors с телом: {}", createdDirector);
        return createdDirector;
    }

    @PutMapping
    public Director updateDirector(@RequestBody Director director) {
        log.info("Пришел PUT запрос /directors с телом: {}", director);
        Director updatedDirector = filmService.updateDirector(director);
        log.info("Отправлен ответ PUT /directors с телом: {}", updatedDirector);
        return updatedDirector;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDirector(@PathVariable Integer id) {
        log.info("Пришел DELETE запрос /directors/{}", id);
        filmService.deleteDirector(id);
        log.info("Режиссёр с ID {} удалён", id);
    }
}

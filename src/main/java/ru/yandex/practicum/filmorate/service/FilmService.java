package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addNewLike(Long filmId, Long userId) {
        User existUser = userStorage.findUserById(userId);
        Film existFilm = filmStorage.findFilmById(filmId);
        checkValid(existUser, existFilm);

        existFilm.getLike().add(userId);
        filmStorage.updateFilm(existFilm);
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        User existUser = userStorage.findUserById(userId);
        Film existFilm = filmStorage.findFilmById(filmId);
        checkValid(existUser, existFilm);

        existFilm.getLike().remove(userId);
        filmStorage.updateFilm(existFilm);
        log.info("Пользователь с ID {} удалил лайк с фильма с ID {}", userId, filmId);
    }

    public Collection<Film> getListOfPopularFilms(Integer count) {
        log.info("Отправлен список популярных фильмов");
        return filmStorage.getAllFilms().stream()
                .filter(film -> film.getLike() != null)
                .sorted((film1, film2) -> Integer.compare(film2.getLike().size(), film1.getLike().size()))
                .limit(count == null ? count : 10)
                .collect(Collectors.toList());
    }

    private void checkValid(User existUser, Film existFilm) {
        if (existUser == null) {
            throw new NotFoundException("Пользователь не найден");

        }
        if (existFilm.getLike() == null) {
            throw new ValidationException("Лайки пользователя не найдены");
        }
    }
}


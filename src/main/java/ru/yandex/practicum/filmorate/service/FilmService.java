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

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.time.Month.DECEMBER;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getAllFilms() {
        log.info("Получаем список все фильмов из хранилища");
        return filmStorage.getAllFilms();
    }

    public Film addNewFilm(Film film) {
        checkValidStorage(film);
        log.info("Добавляем новый фильм в хранилище");
        return filmStorage.addNewFilm(film);
    }

    public Film updateFilm(Film updatedFilm) {
        checkValidStorage(updatedFilm);
        log.info("Обновляем фильм в хранилище");
        return filmStorage.updateFilm(updatedFilm);
    }

    public void addNewLike(Long filmId, Long userId) {
        User existUser = userStorage.findUserById(userId);
        Film existFilm = filmStorage.findFilmById(filmId);
        checkValidService(existUser, existFilm);

        existFilm.getLike().add(userId);
        filmStorage.updateFilm(existFilm);
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        User existUser = userStorage.findUserById(userId);
        Film existFilm = filmStorage.findFilmById(filmId);
        checkValidService(existUser, existFilm);

        existFilm.getLike().remove(userId);
        filmStorage.updateFilm(existFilm);
        log.info("Пользователь с ID {} удалил лайк с фильма с ID {}", userId, filmId);
    }

    public Collection<Film> getListOfPopularFilms(Integer count) {
        Collection<Film> popularFilms = filmStorage.getAllFilms().stream()
                .filter(film -> film.getLike() != null)
                .sorted((film1, film2) -> Integer.compare(film2.getLike().size(), film1.getLike().size()))
                .limit(count == null ? 10 : count)  // исправил проверку на null и лимит
                .collect(Collectors.toList());

        log.info("Отправлен список популярных фильмов: {}", popularFilms);
        return popularFilms;
    }

    private void checkValidStorage(Film film) {
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

    private void checkValidService(User existUser, Film existFilm) {
        if (existUser == null) {
            throw new NotFoundException("Пользователь не найден");

        }
        if (existFilm.getLike() == null) {
            throw new ValidationException("Лайки пользователя не найдены");
        }
    }
}



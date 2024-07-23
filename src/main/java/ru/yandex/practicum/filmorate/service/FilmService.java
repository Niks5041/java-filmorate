package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.Like;
import ru.yandex.practicum.filmorate.model.films.Mpa;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;
import ru.yandex.practicum.filmorate.storage.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;

    public FilmDto getFilmById(Integer id) {
        log.info("Получаем фильм по id: {} из хранилища", id);

        Film film = filmStorage.findFilmById(id);
        genreStorage.addFilmToGenres(film.getId(), film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
        Mpa mpa = mpaStorage.findRatingById(film.getMpa().getId());
        Set<Genre> genres = new LinkedHashSet<>(genreStorage.getGenreByFilmId(id));

        film.setGenres(genres);
        film.setMpa(mpa);

        if (film == null) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }

        return FilmMapper.mapToFilmDto(film);
    }

    public Collection<FilmDto> getAllFilms() {
        log.info("Получаем список все фильмов из хранилища");
        return filmStorage.getAllFilms()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto addNewFilm(Film film) {
        filmValid(film);

        FilmDto filmDto = FilmMapper.mapToFilmDto(filmStorage.addNewFilm(film));
        genreStorage.addFilmToGenres(film.getId(), film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
        Mpa mpa = mpaStorage.findRatingById(film.getMpa().getId());
        Set<Genre> genres = new LinkedHashSet<>(genreStorage.getGenreByFilmId(filmDto.getId()));

        filmDto.setMpa(mpa);
        filmDto.setGenres(genres);

        log.info("Добавлен новый фильм в хранилище");
        return filmDto;
    }

    public FilmDto updateFilm(Film updatedFilm) {
        filmValid(updatedFilm);

        FilmDto filmDto = FilmMapper.mapToFilmDto(filmStorage.updateFilm(updatedFilm));
        genreStorage.addFilmToGenres(updatedFilm.getId(), updatedFilm.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
        Mpa mpa = mpaStorage.findRatingById(updatedFilm.getMpa().getId());

        filmDto.setMpa(mpa);

        log.info("Обновляем фильм в хранилище");
        return filmDto;
    }

    public void addNewLike(Integer filmId, Integer userId) {
        User existUser = userStorage.findUserById(userId);
        Film existFilm = filmStorage.findFilmById(filmId);
        checkValidService(existUser, existFilm);

        Like newLike = new Like();
        newLike.setUserId(existUser.getId());
        newLike.setFilmId(existFilm.getId());
        likeStorage.addLike(newLike);

        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        User existUser = userStorage.findUserById(userId);
        Film existFilm = filmStorage.findFilmById(filmId);
        checkValidService(existUser, existFilm);

        likeStorage.deleteLike(filmId, userId);

        log.info("Пользователь с ID {} удалил лайк с фильма с ID {}", userId, filmId);
    }

    public Collection<FilmDto> getListOfPopularFilms(Integer count) {
        Collection<FilmDto> popularFilms = filmStorage.getAllPopFilms().stream()
                .limit(count == null ? 10 : count)
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());

        log.info("Отправлен список популярных фильмов: {}", popularFilms);
        return popularFilms;
    }

    public Collection<Genre> getAllGenres() {
        log.info("Получаем список всех жанров из хранилища");
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(Integer id) {
        log.info("Получаем информацию о жанре с ID {} из хранилища", id);
       Genre genre = genreStorage.findGenreById(id);
        if (genre != null) {
            return genre;
        } else {
            throw new NotFoundException("Жанр с указанным ID не найден");
        }
    }

    public Mpa getRatingById(Integer id) {
        log.info("Получаем информацию о рейтинге с ID {} из хранилища", id);

        Mpa mpa = mpaStorage.findRatingById(id);
        if (mpa != null) {
            return mpa;
        } else {
            throw new NotFoundException("Рейтинг с указанным ID не найден");
        }
    }

    public Collection<Mpa> getAllRatings() {
        log.info("Получаем список всех рейтингов из хранилища");
        Collection<Mpa> ratings = mpaStorage.getAllRatings();
        return ratings;
    }

    private void filmValid(Film film) {
        Collection<Mpa> ratings = mpaStorage.getAllRatings();
        if (film.getMpa().getId() > ratings.size()) {
            log.info("Такого рейтинга нет в базе данных", film.getMpa().getId());
            throw new ValidationException("Такого рейтинга нет в базе данных");
        }
        if (!genreStorage.checkGenresExist(film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()))) {
            throw new ValidationException("Один или несколько жанров не найдены в базе данных");
        }
    }

    private void checkValidService(User existUser, Film existFilm) {
        if (existUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}



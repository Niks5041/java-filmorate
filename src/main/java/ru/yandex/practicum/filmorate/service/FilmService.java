package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.films.*;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.film.*;
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
    private final DirectorStorage directorStorage;

    public Collection<Director> getAllDirectors() {
        log.info("Получаем список все режиссеров из хранилища");
        return directorStorage.getAllDirectors()
                .stream()
                .collect(Collectors.toList());
    }

    public Director getDirectorById(Integer id) {
        log.info("Получаем режиссера по id: {} из хранилища", id);
        Director director1 = directorStorage.findDirectorById(id);
        if (director1 == null) {
            throw new NotFoundException("Режиссер не найден");
        }
        return director1;
    }

    public Director createDirector(Director director) {
        log.info("Добавляем нового режиссера в хранилище");
        Director director1 = directorStorage.addNewDirector(director);
        log.info("Добавлен новый режиссер в хранилище");
        return director1;
    }

    public Director updateDirector(Director updatedDirector) {
        log.info("Обновляем режиссера в хранилище");
        Director director1 = directorStorage.updateDirector(updatedDirector);
        log.info("Обновлен режиссер в хранилище");
        return director1;
    }

    public void deleteDirector(Integer id) {
        directorStorage.deleteDirectorById(id);
        log.info("Режиссер с ID {} удален из хранилища", id);
    }

    public Collection<FilmDto> getAllFilmsByDirector(Integer id, String[] sortBy) {
        log.info("Получаем список всех фильмов режиссера с ID {} из хранилища", id);

        String param = sortBy[0];
        Collection<Film> films;
        switch (param) {
            case "likes":
                films = filmStorage.getAllFilmsByDirectorAndLikes(id);
                log.info("Получен список всех фильмов режиссера из хранилища по лайкам", films);
                break;
            case "year":
                films = filmStorage.getAllFilmsByDirectorAndYear(id);
                log.info("Получен список всех фильмов режиссера из хранилища по годам", films);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + param);
        }

        /*return films.stream()
                .map(film -> {
                    Set<Director> directors = new LinkedHashSet<>(directorStorage.getDirectorByFilmId(film.getId()));
                    FilmDto filmDto = FilmMapper.mapToFilmDto(film);
                    filmDto.setDirectors(directors);
                    return filmDto;
                })
                .collect(Collectors.toList());*/
        return films.stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto getFilmById(Integer id) {
        log.info("Получаем фильм по id: {} из хранилища", id);

        Film film = filmStorage.findFilmById(id);
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
        directorStorage.addFilmToDirector(film.getId(), film.getDirectors().stream().map(Director::getId).collect(Collectors.toSet()));

        Mpa mpa = mpaStorage.findRatingById(film.getMpa().getId());
        Set<Genre> genres = new LinkedHashSet<>(genreStorage.getGenreByFilmId(filmDto.getId()));
        Set<Director> directors = new LinkedHashSet<>(directorStorage.getDirectorByFilmId(filmDto.getId()));

        filmDto.setMpa(mpa);
        filmDto.setGenres(genres);
        filmDto.setDirectors(directors);

        log.info("Добавлен новый фильм в хранилище");
        return filmDto;
    }

    public FilmDto updateFilm(Film updatedFilm) {
        filmValid(updatedFilm);

        FilmDto filmDto = FilmMapper.mapToFilmDto(filmStorage.updateFilm(updatedFilm));

        genreStorage.addFilmToGenres(updatedFilm.getId(), updatedFilm.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
        directorStorage.addFilmToDirector(updatedFilm.getId(), updatedFilm.getDirectors().stream().map(Director::getId).collect(Collectors.toSet()));

        Set<Genre> genres = new LinkedHashSet<>(genreStorage.getGenreByFilmId(filmDto.getId()));
        Mpa mpa = mpaStorage.findRatingById(updatedFilm.getMpa().getId());
        Set<Director> directors = new LinkedHashSet<>(directorStorage.getDirectorByFilmId(filmDto.getId()));

        filmDto.setMpa(mpa);
        filmDto.setGenres(genres);
        filmDto.setDirectors(directors);

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

    public Collection<FilmDto> getListOfPopularFilms(Integer count, Integer genreId, Integer year) {
        Collection<FilmDto> popularFilms = filmStorage.getAllPopFilms().stream()
                .filter(film -> (genreId == 0) || (film.getGenres().stream().anyMatch(genre -> genre.getId() == genreId)))
                .filter(film -> (year == 0) || (film.getReleaseDate().getYear() == year))
                //.limit(count == null ? 10 : count)
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
        if (count != 0) {
            popularFilms = popularFilms.stream().limit(count).collect(Collectors.toList());
        }
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

    public void deleteFilmById(Integer id) {
        filmStorage.deleteFilmById(id);
        log.info("Фильм с ID {} удален из хранилища", id);
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



package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Like;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@Repository
@Primary
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_FILMS = "SELECT * FROM film";

    private static final String ADD_NEW_FILM = "INSERT INTO film " +
            "(name, description, releaseDate, duration, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM = "UPDATE film SET" +
            " name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ? WHERE id = ?";
    private static final String FIND_FILM = "SELECT * FROM film WHERE id = ?";
//    private static final String FIND_FILM_GENRES = "SELECT * FROM genre JOIN genre ON film.genre_id = genre.id  WHERE id = ?";
    private static final String ADD_LIKE = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов из базы данных");
        List<Film> films = findMany(FIND_ALL_FILMS);
        log.info("Получено {} фильмов из базы данных", films.size());
        return films;
    }

    @Override
    public Film addNewFilm(Film film) {
        log.info("Добавление нового фильма в базу данных: {}", film);
        int id = insert(ADD_NEW_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );

        film.setId(id);
        log.info("Фильм успешно добавлен с ID: {}", id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        log.info("Обновление информации о фильме с ID {}: {}", updatedFilm.getId(), updatedFilm);
        update(UPDATE_FILM,
                updatedFilm.getName(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getId(),
                updatedFilm.getMpa().getId()
        );

        log.info("Информация о фильме с ID {} успешно обновлена", updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    @Override
    public Film findFilmById(Integer id) {
        log.info("Поиск фильма по ID в базе данных {}", id);
        return findOne(FIND_FILM, id);
    }

    @Override
    public void addLike(Like like) {
        log.info("Добавление лайка в базу данных: {}", like);
        jdbc.update(ADD_LIKE, like.getUserId(), like.getFilmId());
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        log.info("Удаление лайка из базы данных: filmId={}, userId={}", filmId, userId);
        jdbc.update(DELETE_LIKE, filmId, userId);
    }
 }

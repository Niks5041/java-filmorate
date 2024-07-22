package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_FILMS = "SELECT * FROM film";

    private static final String ADD_NEW_FILM = "INSERT INTO film " +
            "(name, description, releaseDate, duration, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM = "UPDATE film SET" +
            " name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ? WHERE id = ?";
    private static final String FIND_FILM = "SELECT * FROM film WHERE id = ?";
    private static final String ADD_FILM_TO_GENRE = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

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

        for (Genre genre : film.getGenres()) {
            insert(ADD_FILM_TO_GENRE, id, genre.getId());
        }

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
 }

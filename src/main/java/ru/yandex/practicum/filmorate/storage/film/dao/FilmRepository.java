package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_FILMS_POP = "SELECT F.*, M.NAME as mpa_name, COUNT (L.USER_ID) as likes, GROUP_CONCAT(G.ID) AS genres_ids, GROUP_CONCAT(G.NAME) AS genres " +
            "FROM FILM F " +
            "LEFT JOIN LIKES L ON F.ID = L.FILM_ID " +
            "LEFT JOIN film_genres FG ON F.ID = FG.FILM_ID " +
            "LEFT JOIN genre G ON FG.GENRE_ID = G.ID " +
            "LEFT JOIN MPA M ON F.MPA_ID = M.ID " +
            "GROUP BY F.ID " +
            "ORDER BY likes DESC;";
    private static final String FIND_ALL_FILMS = "select f.*, m.name as mpa_name, group_concat(g.id) as genres_ids, group_concat(g.name) as genres " +
            "from film f " +
            "left join film_genres fg on f.id = fg.film_id " +
            "left join genre g on fg.genre_id = g.id " +
            "left join mpa m on f.mpa_id = m.id " +
            "group by f.id";

    private static final String ADD_NEW_FILM = "INSERT INTO film " +
            "(name, description, releaseDate, duration, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM = "UPDATE film SET" +
            " name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ? WHERE id = ?";
    private static final String FIND_FILM = "SELECT * FROM film f LEFT JOIN mpa m ON f.mpa_id = m.id WHERE f.id = ?";
    private static final String DELETE_FILM_TO_GENRE = "DELETE FROM film_genre WHERE FILM_ID = ?";
    private static final String DELETE_FILM = "DELETE FROM film WHERE ID = ?";

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
    public List<Film> getAllPopFilms() {
        log.info("Запрос на получение всех фильмов из базы данных в порядке убывания популярности");
        List<Film> films = findMany(FIND_ALL_FILMS_POP);
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
                updatedFilm.getMpa().getId(),
                updatedFilm.getId()
        );

        delete(DELETE_FILM_TO_GENRE, updatedFilm.getId());

        log.info("Информация о фильме с ID {} успешно обновлена", updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    @Override
    public Film findFilmById(Integer id) {
        log.info("Поиск фильма по ID в базе данных {}", id);
        return findOne(FIND_FILM, id);
    }

    @Override
    public void deleteFilmById(Integer id) {
        log.info("Удаление фильма с ID {} из базы данных", id);
        delete(DELETE_FILM, id);
    }
}

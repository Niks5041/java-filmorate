package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_FILMS_BY_DIRECTOR_AND_YEAR = "SELECT f.*, m.name as mpa_name, group_concat(d" +
            ".id) as directors_ids, group_concat(d.name) as directors, " + "group_concat(g.id) as genres_ids, " +
            "group_concat(g.name) as genres " + "FROM film f " + "left join film_genre fg on f.id = fg.film_id " +
            "left join genres g on fg.genre_id = g.id " + "left join mpa m on f.mpa_id = m.id " + "LEFT JOIN LIKES l " +
            "ON f.id = l.film_id " + "left JOIN film_director fd ON f.id = fd.film_id " + "left JOIN director d ON fd" +
            ".director_id = d.id " + "WHERE fd.director_id = ? " + "GROUP BY f.id " + "ORDER BY EXTRACT(YEAR FROM f" +
            ".releaseDate) ASC";
    private static final String FIND_ALL_FILMS_BY_DIRECTOR_AND_LIKES = "SELECT f.*, m.name as mpa_name, COUNT (L" +
            ".USER_ID) as likes, group_concat(d.id) as directors_ids, " + "group_concat(d.name) as directors, " +
            "group_concat(g.id) as genres_ids, group_concat(g.name) as " + "genres " + "FROM FILM f " + "left join " +
            "film_genre fg on f.id = fg.film_id " + "left join genres g on fg.genre_id = g.id " + "left join mpa m on" +
            " f.mpa_id = m.id " + "LEFT JOIN LIKES l ON f.id = l.film_id " + "left JOIN film_director fd ON f.id = fd" +
            ".film_id " + "left JOIN director d ON fd.director_id = d.id " + "WHERE fd.director_id = ? " + "GROUP BY " +
            "f.id " + "ORDER BY likes DESC;";
    private static final String FIND_ALL_FILMS_POP =
            "SELECT F.*, M.NAME as mpa_name, COUNT (L.USER_ID) as likes, " + "group_concat(d.id) as directors_ids, " +
                    "group_concat(d.name) as directors, GROUP_CONCAT(G.ID) AS " + "genres_ids, GROUP_CONCAT(G.NAME) " +
                    "AS genres " + "FROM FILM F " + "LEFT JOIN LIKES L ON F.ID = L.FILM_ID " + "LEFT JOIN film_genre " +
                    "FG ON F.ID = FG.FILM_ID " + "LEFT JOIN genres G ON FG.GENRE_ID = G.ID " + "LEFT JOIN MPA M ON F" +
                    ".MPA_ID = M.ID " + "left JOIN film_director fd ON f.id = fd.film_id " + "left JOIN director d ON" +
                    " fd.director_id = d.id " + "GROUP BY F.ID " + "ORDER BY likes DESC;";
    private static final String FIND_ALL_FILMS = "select f.*, m.name as mpa_name, group_concat(d.id) as " +
            "directors_ids, group_concat(d.name) as directors, group_concat(g.id) as genres_ids, group_concat(g.name)"
            + " as genres " + "from film f " + "left join film_genre fg on f.id = fg.film_id " + "left join genres g " +
            "on fg.genre_id = g.id " + "left join mpa m on f.mpa_id = m.id " + "left JOIN film_director fd ON f.id = " +
            "fd.film_id " + "left JOIN director d ON fd.director_id = d.id " + "group by f.id";

    private static final String ADD_NEW_FILM = "INSERT INTO film " + "(name, description, releaseDate, duration, " +
            "mpa_id) " + "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM = "UPDATE film SET " + "name = ?, description = ?, releaseDate = ?, " +
            "duration = ?, mpa_id = ? WHERE id = ?";
    private static final String FIND_FILM =
            "select f.*, m.name as mpa_name, group_concat(d.id) as directors_ids, " + "group_concat(d.name) as " +
                    "directors, group_concat(g.id) as genres_ids, group_concat(g.name) as genres " + "from film f " + "left join film_genre fg on f.id = fg.film_id " + "left join genres g on fg.genre_id = g.id " + "left join mpa m on f.mpa_id = m.id " + "left JOIN film_director fd ON f.id = fd.film_id " + "left JOIN director d ON fd.director_id = d.id " + "where f.id = ? " + "group by f.id ";
    // private static final String DELETE_FILM_TO_GENRE = "DELETE FROM film_genre WHERE FILM_ID = ?";
    private static final String DELETE_FILM_TO_DIRECTOR = "DELETE FROM film_director WHERE film_id = ?;";
    private static final String DELETE_FILM = "DELETE FROM film WHERE id = ?;";
    private static final String FIND_FILM_BY_DIRECTOR = "SELECT f.*, m.name AS mpa_name, GROUP_CONCAT(d.id) AS " +
            "directors_ids, " + "GROUP_CONCAT(d.name) AS directors, GROUP_CONCAT(g.id) AS genres_ids, " +
            "GROUP_CONCAT(g.name) AS genres " + "FROM film f " + "LEFT JOIN likes l ON f.id = l.film_id " + "LEFT " +
            "JOIN film_genre fg ON f.id = fg.film_id " + "LEFT JOIN genres g ON fg.genre_id = g.id " + "LEFT JOIN mpa" +
            " m ON f.mpa_id = m.id " + "LEFT JOIN film_director fd ON f.id = fd.film_id " + "LEFT JOIN director d ON " +
            "fd.director_id = d.id " + "WHERE d.name LIKE ? " + "GROUP BY f.id " + "ORDER BY COUNT(l.user_id) DESC;";
    private static final String FIND_FILM_BY_TITLE = "SELECT f.*, m.name AS mpa_name, GROUP_CONCAT(d.id) AS " +
            "directors_ids, " + "GROUP_CONCAT(d.name) AS directors, GROUP_CONCAT(g.id) AS genres_ids, " +
            "GROUP_CONCAT(g.name) AS genres " + "FROM film f " + "LEFT JOIN likes l ON f.id = l.film_id " + "LEFT " +
            "JOIN film_genre fg ON f.id = fg.film_id " + "LEFT JOIN genres g ON fg.genre_id = g.id " + "LEFT JOIN mpa" +
            " m ON f.mpa_id = m.id " + "LEFT JOIN film_director fd ON f.id = fd.film_id " + "LEFT JOIN director d ON " +
            "fd.director_id = d.id " + "WHERE f.name LIKE ? " + "GROUP BY f.id " + "ORDER BY COUNT(l.user_id) DESC;";
    private static final String FIND_FILM_BY_TITLE_AND_DIRECTOR = "SELECT f.*, m.name AS mpa_name, GROUP_CONCAT(d.id)" +
            " AS directors_ids, " + "GROUP_CONCAT(d.name) AS directors, GROUP_CONCAT(g.id) AS genres_ids, " +
            "GROUP_CONCAT(g.name) AS genres " + "FROM film f " + "LEFT JOIN likes l ON f.id = l.film_id " + "LEFT " +
            "JOIN film_genre fg ON f.id = fg.film_id " + "LEFT JOIN genres g ON fg.genre_id = g.id " + "LEFT JOIN mpa" +
            " m ON f.mpa_id = m.id " + "LEFT JOIN film_director fd ON f.id = fd.film_id " + "LEFT JOIN director d ON " +
            "fd.director_id = d.id " + "WHERE f.name LIKE ? " + "OR d.name LIKE ? " + "GROUP BY f.id " + "ORDER BY " +
            "COUNT(l.user_id) DESC;";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> getAllFilmsByDirectorAndLikes(Integer id) {
        log.info("Запрос на получение всех фильмов режиссера c ID {} из базы данных по лайкам", id);
        List<Film> films = findMany(FIND_ALL_FILMS_BY_DIRECTOR_AND_LIKES, id);
        log.info("Получено {} фильмов из базы данных", films.size());
        return films;
    }

    @Override
    public List<Film> getAllFilmsByDirectorAndYear(Integer id) {
        log.info("Запрос на получение всех фильмов режиссера c ID {} из базы данных по годам", id);
        List<Film> films = findMany(FIND_ALL_FILMS_BY_DIRECTOR_AND_YEAR, id);
        log.info("Получено {} фильмов из базы данных", films.size());
        return films;
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
        int id = insert(ADD_NEW_FILM, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId());
        film.setId(id);
        log.info("Фильм успешно добавлен с ID: {}. Фильм: {}", id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        log.info("Обновление информации о фильме с ID {}: {}", updatedFilm.getId(), updatedFilm);
        update(UPDATE_FILM, updatedFilm.getName(), updatedFilm.getDescription(), updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(), updatedFilm.getMpa().getId(), updatedFilm.getId());

        // delete(DELETE_FILM_TO_DIRECTOR, updatedFilm.getId());

        log.info("Информация о фильме с ID {} успешно обновлена: {}", updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    @Override
    public Film findFilmById(Integer id) {
        log.info("Поиск фильма по ID в базе данных {}", id);
        return findOne(FIND_FILM, id);
    }

    @Override
    public List<Film> findFilmsByDirector(String query) {
        log.info("Поиск фильмов по режиссёру. Запрос {}", query);
        return findMany(FIND_FILM_BY_DIRECTOR, "%" + query + "%");
    }

    @Override
    public List<Film> findFilmsByTitle(String query) {
        log.info("Поиск фильмов по названию. Запрос {}", query);
        return findMany(FIND_FILM_BY_TITLE, "%" + query + "%");
    }

    @Override
    public List<Film> findFilmsByTitleAndDirector(String query) {
        log.info("Поиск фильмов по названию и по режиссёру. Запрос {}", query);
        String likeQuery = "%" + query + "%";
        return findMany(FIND_FILM_BY_TITLE_AND_DIRECTOR, likeQuery, likeQuery);
    }

    @Override
    public void deleteFilmById(Integer id) {
        log.info("Удаление фильма с ID {} из базы данных", id);
        delete(DELETE_FILM, id);
    }

    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        String getCommonLikedFilmsSql = "SELECT f.*, m.name as mpa_name, group_concat(d.id) as directors_ids, " +
                "group_concat(d.name) as directors, group_concat(g.id) as genres_ids, group_concat(g.name) as genres "
                + "FROM film f " + "left join film_genre fg on f.id = fg.film_id " + "left join genres g on fg" +
                ".genre_id = g.id " + "left join mpa m on f.mpa_id = m.id " + "LEFT JOIN LIKES l ON f.id = l.film_id "
                + "left JOIN film_director fd ON f.id = fd.film_id " + "left JOIN director d ON fd.director_id = d.id" +
                " " + "WHERE l.user_id = ? AND l.film_id IN (SELECT film_id FROM likes WHERE user_id = ?) " + "GROUP " +
                "BY f.id";
        log.info("Получение общих фильмов между пользователем с ID {} и другом с ID {}", userId, friendId);
        return findMany(getCommonLikedFilmsSql, userId, friendId);
    }

}

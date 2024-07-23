package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.*;

@Slf4j
@Repository
public class GenreRepository extends BaseRepository<Genre> implements GenreStorage {

    private static final String FIND_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_ALL_GENRES = "SELECT * FROM genres ORDER BY id LIMIT 6";
    private static final String UPDATE_GENRE = "SELECT g.id, g.name " +
            "FROM genres g " +
            "JOIN film_genre fg ON g.id = fg.genre_id " +
            "WHERE fg.film_id = ?";
    private static final String ADD_FILM_TO_GENRE = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String CHECK_GENRES_EXIST = "SELECT COUNT(*) FROM genres WHERE id IN (:genreIds)";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper, NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbc, mapper);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Genre> getAllGenres() {
        log.info("Запрос на получение всех жанров из базы данных");
        Collection<Genre> collection = findMany(FIND_ALL_GENRES);
        Set<Genre> genres = new LinkedHashSet<>(collection);
        log.info("Получено {} жанров из базы данных", genres.size());
        return genres;
    }

    @Override
    public Genre findGenreById(Integer id) {
        log.info("Поиск жанра по ID в базе данных: {}", id);
        return findOne(FIND_GENRE_BY_ID, id);
    }

    @Override
    public List<Genre> getGenreByFilmId(Integer id) {
        log.info("Получаем фильм c ID в базу данных жанров");
        return findMany(UPDATE_GENRE, id);
    }

    public void addFilmToGenres(int filmId, Set<Integer> genreIds) {
        log.info("Добавление фильма с ID={} в жанры: {}", filmId, genreIds);

        List<Object[]> batchParams = new ArrayList<>();
        for (Integer genreId : genreIds) {
            batchParams.add(new Object[]{filmId, genreId});
        }

        jdbc.batchUpdate(ADD_FILM_TO_GENRE, batchParams);

        log.info("Фильм успешно добавлен в жанры");
    }

    public boolean checkGenresExist(Set<Integer> genreIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("genreIds", genreIds);

        Integer count = jdbcTemplate.queryForObject(
                CHECK_GENRES_EXIST,
                parameters,
                Integer.class
        );

        return count != null && count.equals(genreIds.size());
    }
 }


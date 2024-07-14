package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Repository
public class GenreRepository extends BaseRepository<Genre> implements GenreStorage {

    private static final String FIND_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_ALL_GENRES = "SELECT * FROM genres ORDER BY id LIMIT 6";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
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
}

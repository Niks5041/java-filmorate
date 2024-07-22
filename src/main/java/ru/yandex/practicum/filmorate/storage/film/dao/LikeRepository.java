package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Like;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;

@Repository
@Slf4j
public class LikeRepository extends BaseRepository<Like> implements LikeStorage {
    private static final String ADD_LIKE = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String FIND_ALL_BY_FILM_ID = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id " +
            "FROM film f " +
            "JOIN likes l ON f.id = l.film_id " +
            "WHERE f.id = ? " +
            "ORDER BY COUNT(l.id) DESC";


    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
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

//    @Override
//    public Collection<Like> findAllByFilmId(Integer filmId) {
//        log.info("Поиск всех лайков для фильма с ID {}", filmId);
//        return findMany(FIND_ALL_BY_FILM_ID, filmId);
//    }
}

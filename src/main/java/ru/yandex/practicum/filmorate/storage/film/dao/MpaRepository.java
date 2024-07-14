package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;

import java.util.Collection;

@Slf4j
@Repository
public class MpaRepository extends BaseRepository<Mpa> implements MpaStorage {
    private static final String FIND_ALL_RATINGS = "SELECT * FROM mpa LIMIT 5";
    private static final String FIND_RATING_BY_ID = "SELECT * FROM mpa WHERE id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Mpa> getAllRatings() {
        log.info("Запрос на получение всех рейтингов из базы данных");
        Collection<Mpa> mpa = findMany(FIND_ALL_RATINGS);
        log.info("Получено {} рейтингов из базы данных", mpa.size());
        return mpa;
    }

    @Override
    public Mpa findRatingById(Integer id) {
        log.info("Поиск рейтинга по ID в базе данных {}", id);
        return findOne(FIND_RATING_BY_ID, id);
    }
}

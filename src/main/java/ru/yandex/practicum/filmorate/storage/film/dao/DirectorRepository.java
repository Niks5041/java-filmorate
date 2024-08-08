package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Director;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.DirectorStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
public class DirectorRepository extends BaseRepository<Director> implements DirectorStorage {
    private static final String FIND_ALL_DIRECTORS = "SELECT * FROM director";
    private static final String FIND_DIRECTOR_BY_ID = "SELECT * FROM director WHERE id = ?";
    private static final String ADD_NEW_DIRECTOR = "INSERT INTO director (name) VALUES (?)";
    private static final String UPDATE_DIRECTOR = "UPDATE director SET name = ? WHERE id = ?";
    private static final String DELETE_DIRECTOR = "DELETE FROM director WHERE ID = ?";
    private static final String ADD_FILM_TO_DIRECTOR = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";
    private static final String FIND_DIRECTOR_BY_FILM_ID = "SELECT d.id, d.name " +
            "FROM director d " +
            "JOIN film_director fd ON d.id = fd.director_id " +
            "WHERE fd.film_id = ?";

    public DirectorRepository(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public void addFilmToDirector(int filmId, Set<Integer> directorIds) {
        log.info("Добавление фильма с ID={} к режиссерам: {}", filmId, directorIds);

        List<Object[]> batchParams = new ArrayList<>();
        for (Integer dirId : directorIds) {
            batchParams.add(new Object[]{filmId, dirId});
        }

        jdbc.batchUpdate(ADD_FILM_TO_DIRECTOR, batchParams);

        log.info("Фильм успешно добавлен к режиссерам");
    }

    @Override
    public List<Director> getDirectorByFilmId(Integer id) {
        log.info("Получаем режиссера c фильм ID {} в базе данных режиссеров", id);
        return findMany(FIND_DIRECTOR_BY_FILM_ID, id);
    }

    @Override
    public Collection<Director> getAllDirectors() {
        log.info("Запрос на получение всех режиссеров из базы данных");
        Collection<Director> directors = findMany(FIND_ALL_DIRECTORS);
        log.info("Получено {} режиссеров из базы данных", directors.size());
        return directors;
    }

    @Override
    public Director findDirectorById(Integer id) {
        log.info("Поиск режиссера по ID в базе данных {}", id);
        return findOne(FIND_DIRECTOR_BY_ID, id);
    }

    @Override
    public Director addNewDirector(Director director) {
        log.info("Добавление нового режиссера в базу данных: {}", director);
        int id = insert(ADD_NEW_DIRECTOR,
                director.getName()
        );

        director.setId(id);
        log.info("Режиссер успешно добавлен с ID: {}", id, director);
        return director;
    }

    @Override
    public Director updateDirector(Director updatedDirector) {
        log.info("Обновление информации о режиссере с ID {}: {}", updatedDirector.getId(), updatedDirector);
        update(UPDATE_DIRECTOR,
                updatedDirector.getName(),
                updatedDirector.getId()
        );

        log.info("Информация о режиссере с ID {} успешно обновлена", updatedDirector.getId(), updatedDirector);
        return updatedDirector;
    }

    @Override
    public void deleteDirectorById(Integer id) {
        log.info("Удаление режиссера с ID {} из базы данных", id);
        delete(DELETE_DIRECTOR, id);
    }
}

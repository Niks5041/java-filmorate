package ru.yandex.practicum.filmorate.storage.user.dao;

import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.user.EventStorage;

@Slf4j
@Repository
public class EventRepository extends BaseRepository<Event> implements EventStorage {
    public EventRepository(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Event> getEvents(Integer userId) {
        log.info("Запрос на получение всех событий пользователя: [{}] из базы данных", userId);
        List<Event> events = findMany("SELECT * FROM events WHERE user_id = ?", userId);
        log.info("Получено {} событий из базы данных", events.size());
        return events;
    }
}

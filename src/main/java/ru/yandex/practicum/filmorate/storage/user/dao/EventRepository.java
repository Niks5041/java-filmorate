package ru.yandex.practicum.filmorate.storage.user.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;
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

    @Override
    public Integer addEvent(Integer userId, Integer entityId, EventType eventType, Operation operation) {
        log.info("Запись события userId: [{}]; entityId: [{}]; eventType: [{}]; operation: [{}]",
                userId, entityId, eventType, operation);
        Integer id = insert("INSERT INTO EVENTS (USER_ID, ENTITY_ID, EVENT_TYPE, OPERATION, CREATED_AT)" +
                        " VALUES (?, ?, ?, ?, ?)",
                userId, entityId, eventType.getValue(), operation.getValue(), Timestamp.valueOf(LocalDateTime.now()));
        log.info("Идентификатор события {}", id);
        return id;
    }
}

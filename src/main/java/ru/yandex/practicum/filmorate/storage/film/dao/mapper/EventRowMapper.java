package ru.yandex.practicum.filmorate.storage.film.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("id"));
        event.setUserId(rs.getInt("user_id"));
        event.setTimestamp(rs.getTimestamp("created_at"));
        event.setEventType(EventType.valueOf(rs.getInt("event_type")));
        event.setOperation(Operation.valueOf(rs.getInt("operation")));
        event.setEntityId(rs.getInt("entity_id"));

        return event;
    }
}

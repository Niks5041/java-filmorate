package ru.yandex.practicum.filmorate.storage.trigger;

import java.sql.ResultSet;
import java.sql.SQLException;

import ru.yandex.practicum.filmorate.model.event.enums.EventType;

public class LikesTrigger extends EventTrigger {
    public LikesTrigger() {
        super("likes");
    }

    @Override
    public EventType getType() {
        return EventType.LIKE;
    }

    @Override
    public int getEntityId(ResultSet targetRow) throws SQLException {
        return targetRow.getInt("film_id");
    }
}


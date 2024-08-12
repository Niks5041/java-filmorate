package ru.yandex.practicum.filmorate.storage.trigger;

import java.sql.ResultSet;
import java.sql.SQLException;

import ru.yandex.practicum.filmorate.model.event.enums.EventType;

public class FriendsTrigger extends EventTrigger {
    public FriendsTrigger() {
        super("friends");
    }

    @Override
    public EventType getType() {
        return EventType.FRIEND;
    }

    @Override
    public int getEntityId(ResultSet targetRow) throws SQLException {
        return targetRow.getInt("friend_id");
    }
}

package ru.yandex.practicum.filmorate.storage.trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.h2.tools.TriggerAdapter;

import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;

public class EventTrigger extends TriggerAdapter {
    private final String tableName;

    private final Map<String, EventType> tableToEventType = Map.of(
            "likes", EventType.LIKE,
            "friends", EventType.FRIEND,
            "reviews", EventType.REVIEW
    );

    public EventTrigger(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {
        Operation operation = newRow != null && oldRow != null
                ? Operation.UPDATE
                : newRow != null
                ? Operation.ADD
                : oldRow != null
                ? Operation.REMOVE
                : null;
        if (operation == null) {
            throw new IllegalArgumentException("Old and new row are null");
        }

        EventType eventType = tableToEventType.get(tableName);
        if (eventType == null) {
            throw new IllegalArgumentException("Table [" + tableName + "] has not an event type representation.");
        }
        ResultSet targetRow = operation == Operation.REMOVE ? oldRow : newRow;

        // Реагирование на событие
        int userId = targetRow.getInt("user_id");
        int entityId = eventType == EventType.FRIEND
                ? targetRow.getInt("friend_id")
                : targetRow.getInt("id");

        // Вставка в таблицу events
        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO EVENTS (USER_ID, ENTITY_ID, EVENT_TYPE, OPERATION) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, userId);
            statement.setInt(2, entityId);
            statement.setInt(3, eventType.getValue());
            statement.setInt(4, operation.getValue());
            statement.executeUpdate();
        }
    }
}

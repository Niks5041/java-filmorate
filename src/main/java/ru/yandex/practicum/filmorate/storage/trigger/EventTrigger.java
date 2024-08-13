package ru.yandex.practicum.filmorate.storage.trigger;

import org.h2.tools.TriggerAdapter;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class EventTrigger extends TriggerAdapter {
    private final String tableName;

    public EventTrigger(String tableName) {
        this.tableName = tableName;
    }

    public abstract EventType getType();

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

        EventType eventType = this.getType();
        ResultSet targetRow = operation == Operation.REMOVE ? oldRow : newRow;

        // Реагирование на событие
        int userId = targetRow.getInt("user_id");
        int entityId = this.getEntityId(targetRow);

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

    public int getEntityId(ResultSet targetRow) throws SQLException {
        return targetRow.getInt("id");
    }
}


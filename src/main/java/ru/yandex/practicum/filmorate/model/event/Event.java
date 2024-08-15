package ru.yandex.practicum.filmorate.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;

import java.sql.Timestamp;

@Data
public class Event {
    private Integer eventId;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "s")
    private Timestamp timestamp;
    private EventType eventType;
    private Operation operation;
    private Integer userId;
    private Integer entityId;
}

package ru.yandex.practicum.filmorate.model.event;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;

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

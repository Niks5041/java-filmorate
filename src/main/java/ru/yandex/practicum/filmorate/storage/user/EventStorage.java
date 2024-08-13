package ru.yandex.practicum.filmorate.storage.user;

import java.util.Collection;

import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;

public interface EventStorage {
    void deleteEventsByUserId(Integer userId);

    Collection<Event> getEvents(Integer userId);

    Integer addEvent(Integer userId, Integer entityId, EventType eventType, Operation operation);
}

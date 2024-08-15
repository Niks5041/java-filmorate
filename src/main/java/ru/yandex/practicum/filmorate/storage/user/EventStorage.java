package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;

import java.util.Collection;

public interface EventStorage {

    Collection<Event> getEvents(Integer userId);

    Integer addEvent(Integer userId, Integer entityId, EventType eventType, Operation operation);
}

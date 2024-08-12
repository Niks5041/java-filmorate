package ru.yandex.practicum.filmorate.storage.user;

import java.util.Collection;

import ru.yandex.practicum.filmorate.model.event.Event;

public interface EventStorage {
    Collection<Event> getEvents(Integer userId);
}

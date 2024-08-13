package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.event.Event;

import java.util.Collection;

public interface EventStorage {
    Collection<Event> getEvents(Integer userId);
}

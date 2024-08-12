package ru.yandex.practicum.filmorate.storage.trigger;

import ru.yandex.practicum.filmorate.model.event.enums.EventType;

public class ReviewTrigger extends EventTrigger {
    public ReviewTrigger() {
        super("review");
    }

    @Override
    public EventType getType() {
        return EventType.REVIEW;
    }
}

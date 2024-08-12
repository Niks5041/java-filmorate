package ru.yandex.practicum.filmorate.model.event.enums;

public enum EventType {
    LIKE(1), REVIEW(2), FRIEND(3);

    private final int value;

    EventType(int value) {
        this.value = value;
    }

    public static EventType valueOf(int n) {
        return EventType.values()[n - 1];
    }

    public int getValue() {
        return value;
    }
}


package ru.yandex.practicum.filmorate.model.event.enums;

public enum Operation {
    ADD(1), UPDATE(2), REMOVE(3);

    private final int value;

    Operation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

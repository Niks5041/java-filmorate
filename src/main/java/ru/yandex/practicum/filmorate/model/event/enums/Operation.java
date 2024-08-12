package ru.yandex.practicum.filmorate.model.event.enums;

public enum Operation {
    ADD(1), UPDATE(2), REMOVE(3);

    private final int value;

    Operation(int value) {
        this.value = value;
    }

    public static Operation valueOf(int n) {
        return Operation.values()[n - 1];
    }

    public int getValue() {
        return value;
    }
}

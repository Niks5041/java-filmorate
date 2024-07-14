package ru.yandex.practicum.filmorate.model.users;

import lombok.Data;

@Data
public class Friend {
    private Integer userId;
    private Integer friendId;
    private Boolean friendship;
}

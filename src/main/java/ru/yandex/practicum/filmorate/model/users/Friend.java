package ru.yandex.practicum.filmorate.model.users;

import lombok.Data;

@Data
public class Friend {
    private Integer id;
    private Integer userId;
    private Integer friendId;
}

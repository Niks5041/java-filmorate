package ru.yandex.practicum.filmorate.model.users;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Friend {
    @Id
    private Integer id;
    private Integer userId;
    private Integer friendId;
    private Boolean friendship;
}

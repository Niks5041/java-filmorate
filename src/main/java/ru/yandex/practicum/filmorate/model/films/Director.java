package ru.yandex.practicum.filmorate.model.films;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Director {
    private Integer id;
    @NotBlank(message = "Имя режиссера не может быть пустым")
    private String name;
}

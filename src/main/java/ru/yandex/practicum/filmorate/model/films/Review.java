package ru.yandex.practicum.filmorate.model.films;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private int id;
    @Positive(message = "Film ID must be positive")
    private int filmId;
    @Positive(message = "User ID must be positive")
    private int userId;
    @NotNull
    @NotBlank(message = "Content cannot be blank")
    private String content;
    @NotNull
    private boolean isPositive;
    private int useful = 0;
}

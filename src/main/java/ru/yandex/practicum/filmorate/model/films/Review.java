package ru.yandex.practicum.filmorate.model.films;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private int reviewId;
    @NotNull//(message = "Film ID must be positive")
    private Integer filmId;
    @NotNull//(message = "User ID must be positive")
    private Integer userId;
    @NotNull
    @NotBlank(message = "Content cannot be blank")
    private String content;
    @NotNull
    @JsonProperty("isPositive")
    private Boolean isPositive;
    private int useful = 0;
}

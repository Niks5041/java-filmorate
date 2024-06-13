package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {                 //Добырй день! если аннотирую все поля то постман не проходит,если без аннотаций классов model - все ОК, я не верно аннатриую или проверки постман не расщитаны на использование данных аннотаций
    Long id;
    @NotNull
    @NotBlank
    String name;
    @NotNull
    @Size(max = 200)
    String description;
    @NotNull
    LocalDate releaseDate;
    @PositiveOrZero
    Duration duration;
}

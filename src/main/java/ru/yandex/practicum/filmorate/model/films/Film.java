package ru.yandex.practicum.filmorate.model.films;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.films.validator.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class Film {
    private Integer id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Превышена максимальная длина описания фильма (200 символов)")
    private String description;
    @NotNull(message = "Дата релиза не может быть пустой")
    @ReleaseDateConstraint(message = "Дата релиза фильма не может быть в будущем")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;
    @NotNull
    private Mpa mpa;
    @NotNull
    private Set<Genre> genres = new LinkedHashSet<>();
    @NotNull
    private Set<Director> directors = new LinkedHashSet<>();
}

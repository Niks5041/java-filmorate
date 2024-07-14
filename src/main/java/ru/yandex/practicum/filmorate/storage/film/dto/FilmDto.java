package ru.yandex.practicum.filmorate.storage.film.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;
    private List<Genre> genres;
}

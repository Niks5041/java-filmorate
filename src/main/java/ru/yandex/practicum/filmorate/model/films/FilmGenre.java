package ru.yandex.practicum.filmorate.model.films;

import lombok.Data;

@Data
public class FilmGenre {
    private Integer id;
    private Integer filmId;
    private Integer genreId;
}

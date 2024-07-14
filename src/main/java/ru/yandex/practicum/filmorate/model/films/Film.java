package ru.yandex.practicum.filmorate.model.films;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;
    private List<Genre> genres = new LinkedList<>();
    @JsonIgnore
    private Set<Integer> like = new HashSet<>();
}

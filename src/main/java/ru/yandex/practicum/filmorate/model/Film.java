package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Film {                 //Добрый день! если аннотирую все поля аннотациями типа @NonNull по доп заданию то постман не проходит,если без аннотаций классов model - все ОК, я не верно аннатриую или проверки постман не расщитаны на использование данных аннотаций
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
}

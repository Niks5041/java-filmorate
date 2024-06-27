package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();     // верно я понимаю, что если оставить инициализацию на конструктор @data, то будет ошибка так как без явной инициализации в коде он будет null, поэтому инициализорвал сразу, но почему так не понял
}

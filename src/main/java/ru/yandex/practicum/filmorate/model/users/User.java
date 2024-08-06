package ru.yandex.practicum.filmorate.model.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен содержать '@'")
    private String email;
    @NotBlank(message = "Login не может быть пустым")
    private String login;
    @NotBlank(message = "Имя не может быть пустым")
    @NotNull(message = "Имя не может быть null")
    private String name;
    @NotNull(message = "Дата рождения не может быть null")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}

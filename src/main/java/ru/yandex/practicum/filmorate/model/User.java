package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
@Data
public class User {
    Long id;
    @NonNull
    @NotBlank
    @Email
    String email;
    @NonNull
    @NotBlank
    String login;
    @NonNull
    @NotBlank
    String name;
    @NonNull
    @PastOrPresent
    LocalDate birthday;
}

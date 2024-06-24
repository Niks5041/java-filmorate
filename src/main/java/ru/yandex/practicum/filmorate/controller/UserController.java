package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private long generatorId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.info("Запрос на получение списка всех пользователей");
        Collection<User> allUsers = users.values();
        return ResponseEntity.ok().body(allUsers);
    }

    @PostMapping
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление нового пользователя: {}", user);
        checkValid(user);
        user.setId(++generatorId);
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен: {}", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User updatedUser) {
        log.info("Получен запрос на обновление информации о пользователе с ID: {}", updatedUser.getId());
        User oldUser = users.get(updatedUser.getId());
        if (oldUser == null) {
            log.info("Пользователь с ID {} не найден!", updatedUser.getId());
            throw new NotFoundException("Пользователь с ID " + updatedUser.getId() + " не найден!");
        }
        checkValid(updatedUser);
        users.put(updatedUser.getId(), updatedUser);
        log.info("Информация о пользователе успешно обновлена: {}", updatedUser);
        return ResponseEntity.ok().body(updatedUser);
    }

    private void checkValid(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.info("Проверьте правильность заполнения Email пользователя: {}", user);
            throw new ValidationException("Проверьте правильность заполнения Email!");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            log.info("Проверьте правильность заполнения Login пользователя: {}", user);
            throw new ValidationException("Проверьте правильность заполнения Login!");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("В качестве имени пользователя будет использован его логин: {}", user);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения пользователя не может быть в будущем: {}", user);
            throw new ValidationException("Дата рождения пользователя не может быть в будущем!");
        }
    }
}
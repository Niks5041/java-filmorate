package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    // Методы для работы с пользователями

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен запрос на получение списка всех пользователей");
        return userStorage.getAllUsers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User addNewUser(@RequestBody User user) {
        log.info("Получен запрос на добавление нового пользователя: {}", user);
        return userStorage.addNewUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public User updateUser(@RequestBody User updatedUser) {
        log.info("Получен запрос на обновление информации о пользователе с ID: {}", updatedUser.getId());
        return userStorage.updateUser(updatedUser);
    }

    // Методы для работы с друзьями пользователя

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос на добавление в друзья к пользователю с ID {} нового друга с ID {}", userId, friendId);
        userService.addNewFriend(userId, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос на удаление из друзей пользователя с ID {}  друга с ID {}", userId, friendId);
        userService.deleteFriend(userId, friendId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/friends")
    public Collection<User> getFriendsList(@PathVariable Long userId) {
        log.info("Получен запрос на получение списка друзей пользователя с ID {}", userId);
        return userService.getFriendsList(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<User> getCommonFriendsList(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("Получен запрос на получение списка общих друзей пользователя с ID {} и пользователя с ID {}", userId, otherId);
        return userService.getCommonFriendsList(userId, otherId);
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    // Методы для работы с пользователями

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllUsers() {
        log.info("Пришел GET запрос /users");
        Collection<User> users = userService.getAllUsers();
        log.info("Отправлен ответ GET /users с телом: {}", users);
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addNewUser(@RequestBody User user) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        User addedUser = userService.addNewUser(user);
        log.info("Отправлен ответ POST /users с телом: {}", addedUser);
        return addedUser;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User updatedUser) {
        log.info("Пришел PUT запрос /users с телом: {}", updatedUser);
        User updated = userService.updateUser(updatedUser);
        log.info("Отправлен ответ PUT /users с телом: {}", updated);
        return updated;
    }

    // Методы для работы с друзьями пользователя

    @PutMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Пришел PUT запрос /users/{}/friends/{}", userId, friendId);
        userService.addNewFriend(userId, friendId);
        log.info("Отправлен ответ PUT /users/{}/friends/{}", userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Пришел DELETE запрос /users/{}/friends/{}", userId, friendId);
        userService.deleteFriend(userId, friendId);
        log.info("Отправлен ответ DELETE /users/{}/friends/{}", userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriendsList(@PathVariable Long userId) {
        log.info("Пришел GET запрос /users/{}/friends", userId);
        Collection<User> friends = userService.getFriendsList(userId);
        log.info("Отправлен ответ GET /users/{}/friends: {}", userId, friends);
        return friends;
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommonFriendsList(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("Пришел GET запрос /users/{}/friends/common/{}", userId, otherId);
        Collection<User> commonFriends = userService.getCommonFriendsList(userId, otherId);
        log.info("Отправлен ответ GET /users/{}/friends/common/{}: {}", userId, otherId, commonFriends);
        return commonFriends;
    }
}
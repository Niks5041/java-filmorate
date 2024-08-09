package ru.yandex.practicum.filmorate.controller.User;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    // Методы для работы с пользователями

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Пришел GET запрос /users");
        Collection<UserDto> users = userService.getAllUsers();
        log.info("Отправлен ответ GET /users с телом: {}", users);
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(@RequestBody @Valid User user) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        UserDto addedUser = userService.addNewUser(user);
        log.info("Отправлен ответ POST /users с телом: {}", addedUser);
        return addedUser;
    }

    @PutMapping
    public UserDto updateUser(@RequestBody @Valid User updatedUser) {
        log.info("Пришел PUT запрос /users с телом: {}", updatedUser);
        UserDto updated = userService.updateUser(updatedUser);
        log.info("Отправлен ответ PUT /users с телом: {}", updated);
        return updated;
    }

    // Методы для работы с друзьями пользователя

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("Пришел PUT запрос /users/{}/friends/{}", userId, friendId);
        userService.addNewFriend(userId, friendId);
        log.info("Отправлен ответ PUT /users/{}/friends/{}", userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("Пришел DELETE запрос /users/{}/friends/{}", userId, friendId);
        userService.deleteFriend(userId, friendId);
        log.info("Отправлен ответ DELETE /users/{}/friends/{}", userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<UserDto> getFriendsList(@PathVariable Integer userId) {
        log.info("Пришел GET запрос /users/{}/friends", userId);
        List<UserDto> friends = userService.getFriendsList(userId);
        log.info("Отправлен ответ GET /users/{}/friends: {}", userId, friends);
        return friends;
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<UserDto> getCommonFriendsList(@PathVariable Integer userId, @PathVariable Integer otherId) {
        log.info("Пришел GET запрос /users/{}/friends/common/{}", userId, otherId);
        List<UserDto> commonFriends = userService.getCommonFriendsList(userId, otherId);
        log.info("Отправлен ответ GET /users/{}/friends/common/{}: {}", userId, otherId, commonFriends);
        return commonFriends;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Пришел DELETE запрос /users/{}", userId);
        userService.deleteUserById(userId);
        log.info("Отправлен ответ DELETE /users/{}", userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Integer userId) {
        log.info("Пришел GET запрос /users/{}", userId);
        UserDto user = userService.getUserById(userId);
        log.info("Отправлен ответ GET /users/{}: {}", userId, user);
        return user;
    }

    @GetMapping("/{userId}/recommendations")
    public List<FilmDto> getRecommendations(@PathVariable Integer userId) {
        log.info("Пришел GET запрос /users/{}/recommendations", userId);
        List<FilmDto> recommendations = userService.getRecommendations(userId);
        log.info("Отправлен ответ GET /users/{}/recommendations: {}", userId, recommendations);
        return recommendations;
    }
}
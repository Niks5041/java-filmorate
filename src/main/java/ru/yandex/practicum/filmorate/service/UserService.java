package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        log.info("Получаем список всех пользователей из хранилища");
        return userStorage.getAllUsers();
    }

    public User addNewUser(User user) {
        checkValidStorage(user);
        log.info("Добавляем нового пользователя в хранилище");
        return userStorage.addNewUser(user);
    }

    public User updateUser(User updatedUser) {
        checkValidStorage(updatedUser);
        log.info("Обновляем пользователя в хранилище");
        return userStorage.updateUser(updatedUser);
    }

    public void addNewFriend(Long userId, Long friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValidService(existUser, newFriend);

        existUser.getFriends().add(friendId);
        newFriend.getFriends().add(userId);
        userStorage.updateUser(existUser);
        userStorage.updateUser(newFriend);
        log.info("Пользователь с ID {} добавлен в друзья к пользователю с ID {}", friendId, userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValidService(existUser, newFriend);

        existUser.getFriends().remove(friendId);
        newFriend.getFriends().remove(userId);
        userStorage.updateUser(existUser);
        userStorage.updateUser(newFriend);
        log.info("Пользователь с ID {}, удален из друзей  пользователю с ID {}", friendId, userId);
    }

    public Collection<User> getFriendsList(Long userId) {
        User existUser = userStorage.findUserById(userId);
        if (existUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        log.info("Получен список друзей пользователя с ID {}", existUser.getId());
        return existUser.getFriends().stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriendsList(Long userId, Long friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValidService(existUser, newFriend);

        Set<Long> userFriendsList = existUser.getFriends();
        Set<Long> friendFriendsList = newFriend.getFriends();
        userFriendsList.retainAll(friendFriendsList);

        Set<User> commonFriends = userFriendsList.stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toSet());
        log.info("Получен список общих друзей пользовтеля с ID {} с пользовтелем с ID {}", userId, friendId);

        return commonFriends;
    }

    public void checkValidStorage(User user) {
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

    private void checkValidService(User existUser, User newFriend) {
        if (existUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (newFriend == null) {
            throw new NotFoundException("Друг пользователя не найден");
        }
        if (newFriend.getFriends() == null) {
            throw new NotFoundException("Список друзей не найден");
        }
        if (existUser.getFriends() == null) {
            throw new NotFoundException("Список друзей не найден");
        }
    }
}



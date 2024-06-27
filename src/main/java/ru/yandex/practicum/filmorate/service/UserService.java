package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public void addNewFriend(Long userId, Long friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValid(existUser, newFriend);

        existUser.getFriends().add(friendId);
        newFriend.getFriends().add(userId);
        userStorage.updateUser(existUser);
        userStorage.updateUser(newFriend);
        log.info("Пользователь с ID {} добавлен в друзья к пользователю с ID {}", friendId, userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValid(existUser, newFriend);

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
        checkValid(existUser, newFriend);

        Set<Long> userFriendsList = existUser.getFriends();
        Set<Long> friendFriendsList = newFriend.getFriends();
        userFriendsList.retainAll(friendFriendsList);

        Set<User> commonFriends = userFriendsList.stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toSet());
        log.info("Получен список общих друзей пользовтеля с ID {} с пользовтелем с ID {}", userId, friendId);

        return commonFriends;
    }

    private void checkValid(User existUser, User newFriend) {
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

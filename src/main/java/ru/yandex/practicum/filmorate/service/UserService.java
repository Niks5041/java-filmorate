package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public Collection<UserDto> getAllUsers() {
        log.info("Получаем список всех пользователей из хранилища");
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto addNewUser(User user) {
        log.info("Добавляем нового пользователя в хранилище");
        return UserMapper.mapToUserDto(userStorage.addNewUser(user));

    }

    public UserDto updateUser(User updatedUser) {
        log.info("Обновляем пользователя в хранилище");
        return UserMapper.mapToUserDto(userStorage.updateUser(updatedUser));
    }

    public void addNewFriend(Integer userId, Integer friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValidService(existUser, newFriend);
        log.info("Пользователь с ID {} отправил запрос на добавление в друзья к пользователю ID {}", userId, friendId);
        friendStorage.addFriendship(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValidService(existUser, newFriend);
        log.info("Пользователь с ID {} отправил запрос на удаление из друзей пользователя с ID {}", userId, friendId);

        friendStorage.deleteFriendship(userId, friendId);

        log.info("Дружба между пользователями с ID {} и {} успешно удалена", userId, friendId);
    }

    public List<UserDto> getFriendsList(Integer userId) {
        User existUser = userStorage.findUserById(userId);
        if (existUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        log.info("Получен список друзей пользователя с ID {}", existUser.getId());
        return friendStorage.findAllFriendsByUserId(userId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getCommonFriendsList(Integer userId, Integer friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValidService(existUser, newFriend);

        List<UserDto> commonFriends = userStorage.findCommonFriends(userId, friendId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());

        log.info("Получен список общих друзей между пользователями с ID {} и {}: {}", userId, friendId, commonFriends);
        return commonFriends;
    }

    private void checkValidService(User existUser, User newFriend) {
        if (existUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (newFriend == null) {
            throw new NotFoundException("Друг пользователя не найден");
        }
    }
}



package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.users.Friend;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.dto.UserDto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
        validateUser(user);
        log.info("Добавляем нового пользователя в хранилище");
        return UserMapper.mapToUserDto(userStorage.addNewUser(user));

    }

    public UserDto updateUser(User updatedUser) {
        validateUser(updatedUser);
        log.info("Обновляем пользователя в хранилище");
        return UserMapper.mapToUserDto(userStorage.updateUser(updatedUser));
    }

    public void addNewFriend(Integer userId, Integer friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValidService(existUser, newFriend);

//        existUser.getFriends().add(friendId);
//        userStorage.updateUser(existUser);

        log.info("Пользователь с ID {} отправил запрос на добавление в друзья к пользователю ID {}", userId, friendId);

//        if (newFriend.getFriends().contains(existUser.getId())) {
//            newFriend.getFriends().add(userId);
//            userStorage.updateUser(newFriend);
//            log.info("Пользователь с ID {} добавлен в друзья к пользователю с ID {}", userId, friendId);
//        }

        Friend friend = friendStorage.findFriendByUserId(friendId, userId);

        if (friend != null) {
            friend.setFriendId(userId);
            friend.setFriendship(true);
            friendStorage.updateFriendship(friend);
        } else {
            friendStorage.addFriendship(userId, friendId);
        }


    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValidService(existUser, newFriend);

        friendStorage.deleteFriendship(userId, friendId);
        existUser.getFriends().remove(friendId);
        newFriend.getFriends().remove(userId);

        userStorage.updateUser(existUser);
        userStorage.updateUser(newFriend);
        log.info("Пользователь с ID {}, удален из друзей  пользователю с ID {}", friendId, userId);
    }

    public Collection<UserDto> getFriendsList(Integer userId) {
        User existUser = userStorage.findUserById(userId);
        if (existUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        log.info("Получен список друзей пользователя с ID {}", existUser.getId());

            return friendStorage.findAllFriendsByUserId(userId).stream()
                    .map(friend -> {
                        if (friend.getFriendId().equals(userId)) {
                           return friend.getUserId();
                        }
                        return friend.getFriendId();
                    })
                    .map(userStorage::findUserById)
                    .map(UserMapper::mapToUserDto)
                    .collect(Collectors.toList());
    }

    public Collection<UserDto> getCommonFriendsList(Integer userId, Integer friendId) {
        User existUser = userStorage.findUserById(userId);
        User newFriend = userStorage.findUserById(friendId);
        checkValidService(existUser, newFriend);
//
//        Set<Integer> userFriendsList = existUser.getFriends();
//        Set<Integer> friendFriendsList = newFriend.getFriends();
//        userFriendsList.retainAll(friendFriendsList);
//
//        Set<UserDto> commonFriends = userFriendsList.stream()
//                .map(userStorage::findUserById)
//                .map(UserMapper::mapToUserDto)
//                .collect(Collectors.toSet());

        List<UserDto> commonFriends = friendStorage.findAllFriendsByUserId(userId).stream()
                .map(friend -> {
                    if (friend.getFriendId().equals(userId)) {
                        return friend.getUserId();
                    }
                    return friend.getFriendId();
                })
                .map(userStorage::findUserById)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());

        List<UserDto> commonFriends1 = friendStorage.findAllFriendsByUserId(friendId).stream()
                .map(friend -> {
                    if (friend.getFriendId().equals(friendId)) {
                        return friend.getUserId();
                    }
                    return friend.getFriendId();
                })
                .map(userStorage::findUserById)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());

        commonFriends.retainAll(commonFriends1);

        log.info("Получен список общих друзей между пользователями с ID {} и {}: {}", userId, friendId, commonFriends);
        return commonFriends;
    }

    public void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.info("Проверьте правильность заполнения Email пользователя: {}", user);
            throw new ValidationException("Проверьте правильность заполнения Email!");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("В качестве имени пользователя будет использован его логин: {}", user);
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            log.info("Проверьте правильность заполнения Login пользователя: {}", user);
            throw new ValidationException("Проверьте правильность заполнения Login!");
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



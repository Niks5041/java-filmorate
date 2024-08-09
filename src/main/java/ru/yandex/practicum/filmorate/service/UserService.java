package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.dto.UserDto;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;
    private final LikeStorage likeStorage;
    private final FilmStorage filmStorage;

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

    public void deleteUserById(Integer userId) {
        log.info("Удаление пользователя с ID {}", userId);
        userStorage.deleteUserById(userId);
    }

    public List<FilmDto> getRecommendations(Integer userId) {
        log.info("Получение рекомендаций для пользователя с ID {}", userId);
        Map<Integer, List<Integer>> likes = likeStorage.getLikes();
        List<Integer> userLikes = likes.get(userId);
        int maxSimilarityUserId = -1;
        int maxSimilarity = -1;
        if (userLikes != null) {
            for (Map.Entry<Integer, List<Integer>> entry : likes.entrySet()) {
                if (entry.getKey() != userId) {
                    int similarity = calculateSimilarity(likes.get(userId), entry.getValue());
                    if (similarity > maxSimilarity) {
                        maxSimilarity = similarity;
                        maxSimilarityUserId = entry.getKey();
                    }
                }
            }
            if (maxSimilarity == 0 || maxSimilarity == -1) {
                return new ArrayList<>();
            }
            log.info("Пользователь с ID {} наиболее похож на пользователя с ID {}", userId, maxSimilarityUserId);
            List<Integer> recommendedFilmsIds = likes.get(maxSimilarityUserId);
            recommendedFilmsIds.removeAll(likes.get(userId));
            List<FilmDto> recommendedFilms = new ArrayList<>();
            for (Integer filmId : recommendedFilmsIds) {
                recommendedFilms.add(FilmMapper.mapToFilmDto(filmStorage.findFilmById(filmId)));
            }
            return recommendedFilms;
        } else {
            return new ArrayList<>();
        }

    }

    public UserDto getUserById(Integer userId) {
        log.info("Получаем пользователя с ID {}", userId);
        User user = userStorage.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return UserMapper.mapToUserDto(user);
    }

    private void checkValidService(User existUser, User newFriend) {
        if (existUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (newFriend == null) {
            throw new NotFoundException("Друг пользователя не найден");
        }
    }

    private int calculateSimilarity(List<Integer> list1, List<Integer> list2) {
        HashSet<Integer> set1 = new HashSet<>(list1);
        HashSet<Integer> set2 = new HashSet<>(list2);
        set1.retainAll(set2);
        return set1.size();
    }
}



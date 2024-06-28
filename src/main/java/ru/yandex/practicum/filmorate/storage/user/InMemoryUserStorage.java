package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private long generatorId = 0;
    private final Map<Long, User> users = new HashMap<>();

    public User findUserById(Long userId) {
        log.info("Поиск пользователя с ID {}", userId);
        User foundUser = users.values().stream()
                .filter(user -> userId.equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        log.info("Пользователь с ID {} найден: {}", userId, foundUser);
        return foundUser;
    }

    public Collection<User> getAllUsers() {
        Collection<User> usersList = users.values();
        log.info("Получен список всех пользователей{}", usersList);
        return usersList;
    }

    public User addNewUser(User user) {
        user.setId(++generatorId);
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен: {}", user);
        return user;
    }

    public User updateUser(User updatedUser) {
        User oldUser = users.get(updatedUser.getId());
        if (oldUser == null) {
            log.info("Пользователь с ID {} не найден!", updatedUser.getId());
            throw new NotFoundException("Пользователь с ID " + updatedUser.getId() + " не найден!");
        }
        users.put(updatedUser.getId(), updatedUser);
        log.info("Информация о пользователе успешно обновлена: {}", updatedUser);
        return updatedUser;
    }
}


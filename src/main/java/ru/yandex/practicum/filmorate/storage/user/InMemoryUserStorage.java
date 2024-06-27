package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private long generatorId = 0;
    private final Map<Long, User> users = new HashMap<>();

    public User findUserById(Long userId) {
        log.info("Поиск пользователя с ID {}",userId);
        log.info("Пользователь с ID {} найден",userId);
        return users.values().stream()
                .filter(user -> userId.equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
    }

    public Collection<User> getAllUsers() {
        log.info("Получен список всех пользователей{}", users.values());
        return users.values();
    }

    public User addNewUser(User user) {
        checkValid(user);
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
        checkValid(updatedUser);
        users.put(updatedUser.getId(), updatedUser);
        log.info("Информация о пользователе успешно обновлена: {}", updatedUser);
        return updatedUser;
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

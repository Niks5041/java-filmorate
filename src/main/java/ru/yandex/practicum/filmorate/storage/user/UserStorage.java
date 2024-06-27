package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getAllUsers();

    User addNewUser(User user);

    User updateUser(User updatedUser);

    User findUserById(Long userId);
}

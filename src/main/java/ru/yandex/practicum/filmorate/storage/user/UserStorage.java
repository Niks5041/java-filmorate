package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.users.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> getAllUsers();

    User addNewUser(User user);

    User updateUser(User updatedUser);

    User findUserById(Integer userId);

    List<User> findCommonFriends(Integer userId1, Integer userId2);

}

package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.users.User;

import java.util.List;

public interface FriendStorage {

    void addFriendship(Integer userId, Integer friendId);

    void deleteFriendship(Integer userId, Integer friendId);

    List<User> findAllFriendsByUserId(Integer id);

}



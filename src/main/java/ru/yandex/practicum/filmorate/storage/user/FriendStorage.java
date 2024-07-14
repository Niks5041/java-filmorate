package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.users.Friend;

public interface FriendStorage {
    void addFriendship(Integer userId, Integer friendId);

    void deleteFriendship(Integer userId, Integer friendId);

    Friend findFriendById(Integer id);
}



package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.users.Friend;

import java.util.List;

public interface FriendStorage {
    void addFriendship(Integer userId, Integer friendId);
    void updateFriendship(Friend friend);

    void deleteFriendship(Integer userId, Integer friendId);

    List<Friend> findAllFriendsByUserId(Integer id);

    Friend findFriendByUserId(Integer userId, Integer friendId);
}



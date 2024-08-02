package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;

import java.util.List;

@Slf4j
@Repository
public class FriendRepository extends BaseRepository<User> implements FriendStorage {
    private static final String ADD_FRIENDSHIP = "MERGE INTO friends KEY(user_id, friend_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_FRIENDSHIP = "DELETE FROM friends " +
            "WHERE (user_id = ? AND friend_id = ?)";

    private static final String FIND_All_FRIENDS_BY_USER_ID = "SELECT * FROM \"user\" " +
            "JOIN friends f ON \"user\".id = f.friend_id " +
            "WHERE (f.user_id = ?)";

    public FriendRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addFriendship(Integer userId, Integer friendId) {
        update(ADD_FRIENDSHIP, userId, friendId);
        log.info("Добавлена дружба между пользователями с ID {} и {}", userId, friendId);
    }

    @Override
    public void deleteFriendship(Integer userId, Integer friendId) {
        delete(DELETE_FRIENDSHIP,
                userId,
                friendId);
        log.info("Удалена дружба между пользователями с ID {} и {}", userId, friendId);
    }

    @Override
    public List<User> findAllFriendsByUserId(Integer id) {
        return findMany(FIND_All_FRIENDS_BY_USER_ID, id);
    }
}

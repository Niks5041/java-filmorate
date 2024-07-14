package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.users.Friend;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;

@Slf4j
@Repository
@Primary
public class FriendRepository extends BaseRepository<Friend> implements FriendStorage {
    private static final String ADD_FRIENDSHIP = "INSERT INTO friends (user_id, friend_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_FRIENDSHIP = "DELETE FROM friends " +
            "WHERE (user_id = ? AND friend_id = ?)";
    private static final String FIND_FRIEND_BY_ID = "SELECT user_id FROM friends WHERE id = ?";

    public FriendRepository(JdbcTemplate jdbc, RowMapper<Friend> mapper) {
        super(jdbc, mapper);
    }
    @Override
    public void addFriendship(Integer userId, Integer friendId) {
        update(ADD_FRIENDSHIP, userId, friendId);
        log.info("Добавлена дружба между пользователями с ID {} и {}", userId, friendId);
    }
    @Override
    public void deleteFriendship(Integer userId, Integer friendId) {
        update(DELETE_FRIENDSHIP, userId, friendId);
        log.info("Удалена дружба между пользователями с ID {} и {}", userId, friendId);
    }
    @Override
    public Friend findFriendById(Integer id) {
        log.info("Поиск пользователя по ID в базе данных {}", id);
        return findOne(FIND_FRIEND_BY_ID, id);
    }
}

package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.users.Friend;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;

import java.util.List;

@Slf4j
@Repository
@Primary
public class FriendRepository extends BaseRepository<Friend> implements FriendStorage {
    private static final String ADD_FRIENDSHIP = "INSERT INTO friends (user_id, friend_id) " +
            "VALUES (?, ?)";
    private static final String DELETE_FRIENDSHIP = "DELETE FROM friends " +
            "WHERE (user_id = ? AND friend_id = ?)";

    private static final String UPDATE_FRIENDSHIP = "UPDATE FRIENDS " +
            "SET FRIENDSHIP_STATUS = ? " +
            "WHERE id = ?";
    private static final String FIND_FRIEND_BY_ID = "SELECT * FROM friends WHERE id = ?";
    private static final String FIND_FRIEND_BY_USER_ID= "SELECT *  FROM friends WHERE USER_ID = ? AND FRIEND_ID = ? AND FRIENDSHIP_STATUS = FALSE";
    private static final String FIND_All_FRIENDS_BY_USER_ID= "SELECT * FROM friends WHERE FRIENDSHIP_STATUS = TRUE AND (USER_ID = ? OR FRIEND_ID = ?)";

    public FriendRepository(JdbcTemplate jdbc, RowMapper<Friend> mapper) {
        super(jdbc, mapper);
    }
    @Override
    public void addFriendship(Integer userId, Integer friendId) {
        update(ADD_FRIENDSHIP, userId, friendId);
        log.info("Добавлена дружба между пользователями с ID {} и {}", userId, friendId);
    }

    @Override
    public void updateFriendship(Friend friend) {
//        log.info("Обновление информации о пользователе с ID {}: {}", updatedUser.getId(), updatedUser);
        update(UPDATE_FRIENDSHIP,
                friend.getFriendship(),
                friend.getId()
        );
//        log.info("Информация о пользователе с ID {} успешно обновлена", updatedUser.getId());
    }

    @Override
    public void deleteFriendship(Integer userId, Integer friendId) {
        update(DELETE_FRIENDSHIP, userId, friendId);
        log.info("Удалена дружба между пользователями с ID {} и {}", userId, friendId);
    }

    @Override
    public List<Friend> findAllFriendsByUserId(Integer id) {
        return findMany(FIND_All_FRIENDS_BY_USER_ID, id, id);
    }


    @Override
    public Friend findFriendByUserId(Integer userId, Integer friendId) {
        return findOne(FIND_FRIEND_BY_USER_ID, userId, friendId);
    }
}

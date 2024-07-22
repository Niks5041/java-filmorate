package ru.yandex.practicum.filmorate.storage.user.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.users.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class FriendRowMapper implements RowMapper<Friend> {

    @Override
    public Friend mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Friend friend = new Friend();
        friend.setId(resultSet.getInt("id"));
        friend.setUserId(resultSet.getInt("user_id"));
        friend.setFriendId(resultSet.getInt("friend_id"));
        friend.setFriendship(resultSet.getBoolean("friendship_status"));

        return friend;
    }
}



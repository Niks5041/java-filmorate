package ru.yandex.practicum.filmorate.storage.user.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.users.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));

        Timestamp birthdayTimestamp = resultSet.getTimestamp("birthday");
        LocalDate birthday = birthdayTimestamp.toLocalDateTime().toLocalDate();
        user.setBirthday(birthday);

        return user;
    }
}

package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_USERS = "SELECT * FROM \"user\"";
    private static final String ADD_NEW_USER = "INSERT INTO \"user\" " +
            "(name, birthday, email, login) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE \"user\" " +
            "SET name = ?, birthday = ?, email = ?, login = ? " +
            "WHERE id = ?";
    private static final String FIND_USER_BY_ID = "SELECT * FROM \"user\" WHERE id = ?";
    private static final String FIND_COMMON_FRIENDS =
            "SELECT u.* " +
                    "FROM \"user\" u " +
                    "JOIN friends f1 ON u.id = f1.friend_id " +
                    "JOIN friends f2 ON u.id = f2.friend_id " +
                    "WHERE f1.user_id = ? AND f2.user_id = ?";
    private static final String DELETE_USER = "DELETE FROM \"user\" WHERE ID = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
        public Collection<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей из базы данных");
        List<User> users = findMany(FIND_ALL_USERS);
        log.info("Получено {} пользователей из базы данных", users.size());
        return users;
    }

    @Override
    public User addNewUser(User user) {
        log.info("Добавление нового пользователя в базу данных: {}", user);
        int id = insert(ADD_NEW_USER,
                user.getName(),
                user.getBirthday(),
                user.getEmail(),
                user.getLogin()
        );
        user.setId(id);
        log.info("Пользователь успешно добавлен с ID: {}", id);
        return user;
    }

    @Override
    public User updateUser(User updatedUser) {
        log.info("Обновление информации о пользователе с ID {}: {}", updatedUser.getId(), updatedUser);
        update(UPDATE_USER,
                updatedUser.getName(),
                updatedUser.getBirthday(),
                updatedUser.getEmail(),
                updatedUser.getLogin(),
                updatedUser.getId()
        );
        log.info("Информация о пользователе с ID {} успешно обновлена", updatedUser.getId());
        return updatedUser;
    }

    @Override
    public User findUserById(Integer id) {
        log.info("Поиск пользователя по ID в базе данных {}", id);
        return findOne(FIND_USER_BY_ID, id);
    }
    @Override
    public void deleteUserById(Integer id) {
        log.info("Удаление пользователя с ID {} из базы данных", id);
        delete(DELETE_USER, id);
    }

    public List<User> findCommonFriends(Integer userId1, Integer userId2) {
        log.info("Поиск общих друзей между пользователями с ID {} и {}", userId1, userId2);
        List<User> commonFriends = findMany(FIND_COMMON_FRIENDS, userId1, userId2);
        log.info("Найдено {} общих друзей между пользователями с ID {} и {}", commonFriends.size(), userId1, userId2);
        return commonFriends;
    }
 }

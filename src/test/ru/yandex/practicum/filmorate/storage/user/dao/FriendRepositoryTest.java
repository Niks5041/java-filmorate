package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.dao.mapper.FriendRowMapper;
import ru.yandex.practicum.filmorate.storage.user.dao.mapper.UserRowMapper;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({UserRepository.class, FriendRepository.class, FriendRowMapper.class,
        UserRepository.class, UserRowMapper.class})
@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendRepositoryTest {

    private final FriendStorage friendRepository;
    private final UserStorage userStorage;

    @Test
    public void testAddFriendship() {
        User newUser = createUser("New User1", "newuser@example.com", "newuser",
                LocalDate.of(1985, 12, 25));
        userStorage.addNewUser(newUser);
        User newUser2 = createUser("New User2", "newuser@example.com", "newuser2",
                LocalDate.of(1985, 12, 25));
        userStorage.addNewUser(newUser2);

        int userId = newUser.getId();
        int friendId = newUser2.getId();

        friendRepository.addFriendship(userId, friendId);

        List<User> friends = friendRepository.findAllFriendsByUserId(userId);

        assertThat(friends).isNotEmpty();
        assertThat(friends).hasSize(1);
        assertThat(friends.get(0).getId()).isEqualTo(friendId);
    }

    @Test
    public void testDeleteFriendship() {
        User newUser = createUser("New User", "newuser@example.com", "newuser",
                LocalDate.of(1985, 12, 25));
        userStorage.addNewUser(newUser);
        User newUser2 = createUser("New User2", "newuser@example.com", "newuser2",
                LocalDate.of(1985, 12, 25));
        userStorage.addNewUser(newUser2);

        int userId = newUser.getId();
        int friendId = newUser2.getId();

        friendRepository.addFriendship(userId, friendId);
        friendRepository.deleteFriendship(userId, friendId);

        List<User> friends = friendRepository.findAllFriendsByUserId(userId);

        assertThat(friends).isEmpty();
    }

    @Test
    public void testFindAllFriendsByUserId() {
        User newUser = createUser("New User", "newuser@example.com", "newuser",
                LocalDate.of(1985, 12, 25));
        User newUser2 = createUser("New User2", "newuser@example.com", "newuser2",
                LocalDate.of(1985, 12, 25));
        User newUser3 = createUser("New User3", "newuser@example.com", "newuser3",
                LocalDate.of(1985, 12, 25));

        userStorage.addNewUser(newUser);
        userStorage.addNewUser(newUser3);
        userStorage.addNewUser(newUser2);

        int userId = newUser.getId();
        int friendId1 = newUser2.getId();
        int friendId2 = newUser3.getId();

        friendRepository.addFriendship(userId, friendId1);
        friendRepository.addFriendship(userId, friendId2);

        List<User> friends = friendRepository.findAllFriendsByUserId(userId);

        assertThat(friends).hasSize(2);
    }

    private User createUser(String name, String email, String login, LocalDate birthday) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setLogin(login);
        user.setBirthday(birthday);
        return user;
    }
}


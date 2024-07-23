package ru.yandex.practicum.filmorate.storage.user.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.dao.mapper.UserRowMapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import({UserRepository.class, UserRowMapper.class})
@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {

    private final UserStorage userStorage;

    @Test
    public void testAddNewUser() {
        User newUser = createUser("New User", "newuser@example.com", "newuser",
                LocalDate.of(1985, 12, 25));

        User addedUser = userStorage.addNewUser(newUser);

        assertThat(addedUser.getId()).isNotNull();
        assertThat(addedUser.getName()).isEqualTo(newUser.getName());
    }

    @Test
    public void testUpdateUser() {
        User userToUpdate = createUser("UserToUpdate", "update@example.com", "updateuser",
                LocalDate.of(1988, 8, 8));
        userStorage.addNewUser(userToUpdate);

        userToUpdate.setName("Updated Name");
        userToUpdate.setEmail("updated@example.com");

        User updatedUser = userStorage.updateUser(userToUpdate);

        assertThat(updatedUser.getId()).isEqualTo(userToUpdate.getId());
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    public void testFindUserById() {
        User user = createUser("Find User", "finduser@example.com", "finduser",
                LocalDate.of(1980, 3, 10));
        userStorage.addNewUser(user);

        Optional<User> foundUser = Optional.ofNullable(userStorage.findUserById(user.getId()));

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(user.getId());
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
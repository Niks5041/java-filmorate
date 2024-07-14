package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
//    private final UserStorage userStorage;
//
//    @Test
//    public void testFindUserById() {
//
//        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(1l));
//
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
//                );
//    }
}
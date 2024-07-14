package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.user.dto.UserDto;

public final class UserMapper {
    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setLogin(user.getLogin());
        dto.setName(user.getName());
        dto.setBirthday(user.getBirthday());
        return dto;
    }
}

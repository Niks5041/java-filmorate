package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.films.Like;

public interface LikeStorage {
    void addLike(Like like);

    void deleteLike(Integer filmId, Integer userId);
}

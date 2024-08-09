package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.films.Like;

import java.util.List;
import java.util.Map;

public interface LikeStorage {
    void addLike(Like like);

    void deleteLike(Integer filmId, Integer userId);

    Map<Integer, List<Integer>> getLikes();
}

package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.films.Like;

import java.util.Collection;

public interface LikeStorage {
    void addLike(Like like);

    void deleteLike(Integer filmId, Integer userId);
//    Collection<Like> findAllByFilmId(Integer filmId);
}

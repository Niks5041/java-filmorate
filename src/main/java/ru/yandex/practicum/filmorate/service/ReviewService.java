package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;
import ru.yandex.practicum.filmorate.model.films.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.EventStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final EventStorage eventStorage;

    public Review addNewReview(Review review) {
        log.info("Добавление нового отзыва: {}", review);
        if (review.getFilmId() <= 0 || review.getUserId() <= 0) {
            throw new NotFoundException("Неправильный id фильма или пользователя");
        }
        if (review.getFilmId() == 0 || review.getUserId() == 0) {
            throw new NotFoundException("Неправильный id фильма или пользователя");
        }
        Review newReview = reviewStorage.addNewReview(review);
        eventStorage.addEvent(review.getUserId(), newReview.getReviewId(), EventType.REVIEW, Operation.ADD);
        return newReview;
    }

    public Review updateReview(Review review) {
        log.info("Обновление отзыва с id {}: {}", review.getReviewId(), review);
        reviewStorage.updateReview(review);
        Review updateReview = reviewStorage.getReviewById(review.getReviewId());
        eventStorage.addEvent(updateReview.getUserId(), updateReview.getReviewId(), EventType.REVIEW, Operation.UPDATE);
        return updateReview;
    }

    public void deleteReviewById(Integer id) {
        log.info("Удаление отзыва с id: {}", id);
        Review review = reviewStorage.getReviewById(id);
        reviewStorage.deleteReviewById(id);
        eventStorage.addEvent(review.getUserId(), review.getReviewId(), EventType.REVIEW, Operation.REMOVE);
    }

    public Review getReviewById(Integer id) {
        log.info("Получение отзыва с id: {}", id);
        return reviewStorage.getReviewById(id);
    }

    public Collection<Review> getAllReviews() {
        log.info("Получение всех отзывов");
        return reviewStorage.getAllReviews();
    }

    public Collection<Review> getFilmReviewsByFilmId(int filmId) {
        filmStorage.findFilmById(filmId);
        log.info("Получение всех отзывов для фильма с id: {}", filmId);
        return reviewStorage.getAllFilmReviews(filmId);
    }

    public void addLike(Integer reviewId, Integer userId) {
        validateReviewExists(reviewId);
        userStorage.findUserById(userId);
        log.info("Пользователь с id {} поставил лайк отзыву с id {}", userId, reviewId);
        reviewStorage.likeReview(reviewId, userId);
        eventStorage.addEvent(userId, reviewId, EventType.REVIEW, Operation.UPDATE);
    }

    public void addDislike(Integer reviewId, Integer userId) {
        validateReviewExists(reviewId);
        userStorage.findUserById(userId);
        log.info("Пользователь с id {} поставил дизлайк отзыву с id {}", userId, reviewId);
        reviewStorage.dislikeReview(reviewId, userId);
        eventStorage.addEvent(userId, reviewId, EventType.REVIEW, Operation.UPDATE);
    }

    public void removeLike(Integer reviewId, Integer userId) {
        validateReviewExists(reviewId);
        userStorage.findUserById(userId);
        log.info("Пользователь с id {} убрал лайк у отзыва с id {}", userId, reviewId);
        reviewStorage.removeLike(reviewId, userId);
        eventStorage.addEvent(userId, reviewId, EventType.REVIEW, Operation.UPDATE);
    }

    public void removeDislike(Integer reviewId, Integer userId) {
        validateReviewExists(reviewId);
        userStorage.findUserById(userId);
        log.info("Пользователь с id {} убрал дизлайк у отзыва с id {}", userId, reviewId);
        reviewStorage.removeDislike(reviewId, userId);
        eventStorage.addEvent(userId, reviewId, EventType.REVIEW, Operation.UPDATE);
    }

    private void validateReviewExists(Integer reviewId) {
        if (reviewStorage.getReviewById(reviewId) == null) {
            log.error("Отзыв с id {} не найден", reviewId);
            throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
        }
    }
}

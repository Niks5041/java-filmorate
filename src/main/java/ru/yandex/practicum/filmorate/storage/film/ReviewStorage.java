package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.films.Review;

import java.util.Collection;

public interface ReviewStorage {
    Collection<Review> getAllReviews();

    Collection<Review> getAllFilmReviews(int filmId);

    Collection<Review> getAllUserReviews(int userId);

    Review getReviewById(int reviewId);

    Review addNewReview(Review review);

    Review updateReview(Review updatedReview);

    void deleteReviewById(Integer id);

    void likeReview(int reviewId, int userId);

    void dislikeReview(int reviewId, int userId);

    void removeLike(int reviewId, int userId);

    void removeDislike(int reviewId, int userId);
}

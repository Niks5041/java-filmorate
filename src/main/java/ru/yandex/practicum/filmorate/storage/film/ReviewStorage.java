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

    void likeReview(int reviewId);

    void dislikeReview(int reviewId);

    void removeLike(int reviewId);

    void removeDislike(int reviewId);
}

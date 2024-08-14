package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.films.Review;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.film.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.mapper.ReviewRowMapper;

import java.util.Collection;

@Slf4j
@Repository
public class ReviewRepository extends BaseRepository<Review> implements ReviewStorage {

    @Autowired
    public ReviewRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, new ReviewRowMapper());
    }

    @Override
    public Collection<Review> getAllReviews() {
        String sql = "SELECT r.id, r.is_positive, r.useful, r.content, r.film_id, r.user_id FROM review r";
        return findMany(sql);
    }

    @Override
    public Collection<Review> getAllFilmReviews(int filmId) {
        String sql = "SELECT r.id, r.is_positive, r.useful, r.content, r.film_id, r.user_id FROM review r WHERE r.film_id = ?";
        return findMany(sql, filmId);
    }

    @Override
    public Collection<Review> getAllUserReviews(int userId) {
        String sql = "SELECT r.id, r.is_positive, r.useful, r.content, r.film_id, r.user_id FROM review r WHERE r.user_id = ?";
        return findMany(sql, userId);
    }

    @Override
    public Review getReviewById(int reviewId) {
        String sql = "SELECT r.id, r.is_positive, r.useful, r.content, r.film_id, r.user_id FROM review r WHERE r.id = ?";
        Review review = null;
        try {
            review = findOne(sql, reviewId);
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка целостности данных при попытке получить отзыв с id {}", reviewId, e);
        }

        if (review == null) {
            log.error("Отзыв с id {} не найден", reviewId);
            throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
        }
        return review;
    }

    @Override
    @Transactional
    public Review addNewReview(Review review) {
        String insertReviewSql = "INSERT INTO review (is_positive, useful, content, film_id, user_id) VALUES (?, ?, ?, ?, ?)";
        int reviewId;
        try {
            reviewId = insert(insertReviewSql, review.getIsPositive(), review.getUseful(), review.getContent(), review.getFilmId(), review.getUserId());
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка целостности данных при добавлении отзыва", e);
            throw new RuntimeException("Не удалось добавить отзыв из-за ошибки целостности данных", e);
        }
        review.setReviewId(reviewId);
        log.info("Добавлен новый отзыв с id {}", reviewId);
        return review;
    }

    @Override
    public Review updateReview(Review updatedReview) {
        String updateReviewSql = "UPDATE review SET is_positive = ?, content = ? WHERE id = ?";
        try {
            update(updateReviewSql, updatedReview.getIsPositive(), updatedReview.getContent(), updatedReview.getReviewId());
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка целостности данных при обновлении отзыва с id {}", updatedReview.getReviewId(), e);
            throw new RuntimeException("Не удалось обновить отзыв из-за ошибки целостности данных", e);
        }

        log.info("Отзыв с id {} обновлен", updatedReview.getReviewId());
        return updatedReview;
    }

    @Override
    @Transactional
    public void deleteReviewById(Integer id) {
        String deleteReviewSql = "DELETE FROM review WHERE id = ?";
        try {
            delete(deleteReviewSql, id);
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка целостности данных при удалении отзыва с id {}", id, e);
            throw new RuntimeException("Не удалось удалить отзыв из-за ошибки целостности данных", e);
        }
        log.info("Отзыв с id {} удален", id);
    }

    @Override
    @Transactional
    public void likeReview(int reviewId, int userId) {
        String updateUsefulSql = "UPDATE review SET useful = useful + 1 WHERE id = ?";
        String addReviewLikeSql = "INSERT INTO review_likes (review_id, user_id, is_positive) VALUES (?, ?, ?)";
        if (isInteractedByUser(reviewId, userId)) {
            if (!isInteractionPositive(reviewId, userId)) {
                updateUsefulSql = "UPDATE review SET useful = useful + 2 WHERE id = ?";
                addReviewLikeSql = "UPDATE review_likes SET is_positive = TRUE WHERE review_id = ? AND user_id = ?";
                jdbc.update(addReviewLikeSql, reviewId, userId);
            } else jdbc.update(addReviewLikeSql, reviewId, userId, true);
        } else jdbc.update(addReviewLikeSql, reviewId, userId, true);
        try {
            jdbc.update(updateUsefulSql, reviewId);
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка целостности данных при добавлении лайка к отзыву с id {}", reviewId, e);
            throw new RuntimeException("Не удалось добавить лайк из-за ошибки целостности данных", e);
        }
        log.info("Отзыв с id {} оценен положительно, счетчик полезности увеличен", reviewId);
    }

    @Override
    @Transactional
    public void dislikeReview(int reviewId, int userId) {
        String updateUsefulSql = "UPDATE review SET useful = useful - 1 WHERE id = ?";
        String addReviewDislikeSql = "INSERT INTO review_likes (review_id, user_id, is_positive) VALUES (?, ?, ?)";
        if (isInteractedByUser(reviewId, userId)) {
            if (isInteractionPositive(reviewId, userId)) {
                updateUsefulSql = "UPDATE review SET useful = useful - 2 WHERE id = ?";
                addReviewDislikeSql = "UPDATE review_likes SET is_positive = FALSE WHERE review_id = ? AND user_id = ?";
                jdbc.update(addReviewDislikeSql, reviewId, userId);
            } else jdbc.update(addReviewDislikeSql, reviewId, userId, false);
        } else jdbc.update(addReviewDislikeSql, reviewId, userId, false);
        try {
            jdbc.update(updateUsefulSql, reviewId);
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка целостности данных при добавлении дизлайка к отзыву с id {}", reviewId, e);
            throw new RuntimeException("Не удалось добавить дизлайк из-за ошибки целостности данных", e);
        }
        log.info("Отзыв с id {} оценен отрицательно, счетчик полезности уменьшен", reviewId);
        Review review = getReviewById(reviewId);
        log.info(review.toString());
    }

    @Override
    @Transactional
    public void removeLike(int reviewId, int userId) {
        String updateUsefulSql = "UPDATE review SET useful = useful - 1 WHERE id = ?";
        String removeReviewLikeSql = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ?";
        if (!isInteractedByUser(reviewId, userId)) {
            return;
        }
        try {
            jdbc.update(updateUsefulSql, reviewId);
            jdbc.update(removeReviewLikeSql, reviewId, userId);
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка целостности данных при удалении лайка с отзыва с id {}", reviewId, e);
            throw new RuntimeException("Не удалось удалить лайк из-за ошибки целостности данных", e);
        }
        log.info("Лайк убран с отзыва с id {}, счетчик полезности уменьшен", reviewId);
    }

    @Override
    @Transactional
    public void removeDislike(int reviewId, int userId) {
        String updateUsefulSql = "UPDATE review SET useful = useful + 1 WHERE id = ?";
        String removeReviewLikeSql = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ?";
        if (!isInteractedByUser(reviewId, userId)) {
            return;
        }
        try {
            jdbc.update(updateUsefulSql, reviewId);
            jdbc.update(removeReviewLikeSql, reviewId, userId);
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка целостности данных при удалении дизлайка с отзыва с id {}", reviewId, e);
            throw new RuntimeException("Не удалось удалить дизлайк из-за ошибки целостности данных", e);
        }
        log.info("Дизайк убран с отзыва с id {}, счетчик полезности уменьшен", reviewId);
    }

    private boolean isInteractedByUser(int reviewId, int userId) {
        String findUserLikeSql = "SELECT COUNT(*) FROM review_likes WHERE review_id = ? AND user_id = ?";
        try {
            int count = jdbc.queryForObject(findUserLikeSql, new Object[]{reviewId, userId}, Integer.class);
            return count > 0;
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка целостности данных при проверке наличия лайка отзыва с id {}", reviewId, e);
            throw new RuntimeException("Не удалось проверить наличие лайка из-за ошибки целостности данных", e);
        }
    }

    private boolean isInteractionPositive(int reviewId, int userId) {
        String findUserInteractionSql = "SELECT is_positive FROM review_likes WHERE review_id = ? AND user_id = ?";
        try {
            Boolean isPositive = jdbc.queryForObject(findUserInteractionSql, new Object[]{reviewId, userId}, Boolean.class);
            return isPositive != null && isPositive;
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка целостности данных при проверке is_positive отзыва с id {}", reviewId, e);
            throw new RuntimeException("Не удалось проверить is_positive из-за ошибки целостности данных", e);
        }
    }
}

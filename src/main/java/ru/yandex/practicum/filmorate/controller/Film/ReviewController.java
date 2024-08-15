package ru.yandex.practicum.filmorate.controller.Film;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.films.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addReview(@RequestBody @Valid Review review) {
        log.info("Пришел POST запрос на добавление /reviews с телом: {}", review);
        Review addedReview = reviewService.addNewReview(review);
        log.info("Отправлен ответ POST /reviews: {}", addedReview);
        return addedReview;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Review updateReview(@RequestBody @Valid Review review) {
        log.info("Пришел PUT запрос на обновление /reviews с телом: {}", review);
        Review updatedReview = reviewService.updateReview(review);
        log.info("Отправлен ответ PUT /reviews: {}", updatedReview);
        return updatedReview;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Integer id) {
        log.info("Пришел DELETE запрос /reviews/{}", id);
        reviewService.deleteReviewById(id);
        log.info("Отправлен ответ DELETE /reviews/{}", id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review getReviewById(@PathVariable Integer id) {
        log.info("Пришел GET запрос /reviews/{}", id);
        Review review = reviewService.getReviewById(id);
        log.info("Отправлен ответ GET /reviews/{} c телом: {}", id, review);
        return review;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Review> getReviews(
            @RequestParam(required = false) Integer filmId,
            @RequestParam(defaultValue = "10") Integer count) {
        log.info("Пришел GET запрос /reviews с параметрами filmId={} и count={}", filmId, count);
        List<Review> reviews;
        if (filmId != null) {
            reviews = reviewService.getFilmReviewsByFilmId(filmId).stream().toList();
        } else {
            reviews = reviewService.getAllReviews().stream().toList();
        }
        count = Math.min(count, reviews.size());
        Collection<Review> resultReviews = reviews.subList(0, count);
        log.info("Отправлен ответ GET /reviews: {}", resultReviews);
        return resultReviews;
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Пришел PUT запрос /reviews/{}/like/{}", id, userId);
        reviewService.addLike(id, userId);
        log.info("Отправлен ответ PUT /reviews/{}/like/{}", id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Пришел PUT запрос /reviews/{}/dislike/{}", id, userId);
        reviewService.addDislike(id, userId);
        log.info("Отправлен ответ PUT /reviews/{}/dislike/{}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Пришел DELETE запрос /reviews/{}/like/{}", id, userId);
        reviewService.removeLike(id, userId);
        log.info("Отправлен ответ DELETE /reviews/{}/like/{}", id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeDislike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Пришел DELETE запрос /reviews/{}/dislike/{}", id, userId);
        reviewService.removeDislike(id, userId);
        log.info("Отправлен ответ DELETE /reviews/{}/dislike/{}", id, userId);
    }
}

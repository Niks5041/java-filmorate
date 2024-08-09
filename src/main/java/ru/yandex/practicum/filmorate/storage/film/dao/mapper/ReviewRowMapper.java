package ru.yandex.practicum.filmorate.storage.film.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.films.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewRowMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("id"));
        review.setContent(rs.getString("content"));
        review.setIsPositive(rs.getBoolean("is_positive"));
        review.setUseful(rs.getInt("useful"));
        review.setFilmId(rs.getInt("film_id"));
        review.setUserId(rs.getInt("user_id"));
        return review;
    }
}

package ru.yandex.practicum.filmorate.storage.film.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));

        Timestamp releaseDateTimestamp = resultSet.getTimestamp("releaseDate");
        LocalDate releaseDate = releaseDateTimestamp.toLocalDateTime().toLocalDate();
        film.setReleaseDate(releaseDate);

        film.setDuration(resultSet.getInt("duration"));

        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("mpa_id"));
        film.setMpa(mpa);

//        List<Genre> genreSet = new LinkedList<>();
//        Genre genre = new Genre();
//        genreSet.add(genre);
//        film.setGenres(genreSet);

        return film;
    }
}

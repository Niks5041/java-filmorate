package ru.yandex.practicum.filmorate.storage.film.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.Director;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
        mpa.setName(resultSet.getString("mpa_name"));
        film.setMpa(mpa);
        // Set Genres
        String genresString = resultSet.getString("genres");
        String genresIdsString = resultSet.getString("genres_ids");
        if (genresIdsString != null) {
            String[] genreIds = genresIdsString.split(",");
            String[] genreNames = genresString.split(",");
            Set<Genre> genres = new HashSet<>();
            for (int i = 0; i < genreIds.length; i++) {
                Genre genre = new Genre();
                genre.setId(Integer.parseInt(genreIds[i]));
                genre.setName(genreNames[i].trim());
                genres.add(genre);
            }
            film.setGenres(genres);
        }
        // Set directors

        String directorsString = resultSet.getString("directors");
        String directorsIdsString = resultSet.getString("directors_ids");
        if (directorsIdsString != null) {
            String[] directorIds = directorsIdsString.split(",");
            String[] directorNames = directorsString.split(",");
            Set<Director> directors = new HashSet<>();
            for (int i = 0; i < directorIds.length; i++) {
                Director director = new Director();
                director.setId(Integer.parseInt(directorIds[i]));
                director.setName(directorNames[i].trim());
                directors.add(director);
            }
            film.setDirectors(directors);
        }

        return film;
    }
}

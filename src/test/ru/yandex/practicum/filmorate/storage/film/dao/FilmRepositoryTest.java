package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.dao.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.film.dao.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.film.dao.mapper.MpaRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({FilmRepository.class, GenreRepository.class, MpaRepository.class, FilmRowMapper.class, GenreRowMapper.class,
        MpaRowMapper.class})
@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmRepositoryTest {

    private final FilmStorage filmStorage;

    private Film testFilm;

    @BeforeEach
    void setUp() {
        testFilm = new Film();
        testFilm.setName("Test Film");
        testFilm.setDescription("Test Description");
        testFilm.setReleaseDate(LocalDate.of(2023, 1, 1));
        testFilm.setDuration(120);
        Mpa testMpa = new Mpa();
        testMpa.setId(1);
        testFilm.setMpa(new Mpa());
        Genre testGenre = new Genre();
        testGenre.setId(1);
        testFilm.setGenres(Set.of(testGenre));
    }

    @Test
    void testAddNewFilm() {
        Film addedFilm = filmStorage.addNewFilm(testFilm);

        assertThat(addedFilm.getId()).isNotNull();
        assertThat(addedFilm.getName()).isEqualTo(testFilm.getName());
        assertThat(addedFilm.getDescription()).isEqualTo(testFilm.getDescription());
        assertThat(addedFilm.getReleaseDate()).isEqualTo(testFilm.getReleaseDate());
        assertThat(addedFilm.getDuration()).isEqualTo(testFilm.getDuration());
        assertThat(addedFilm.getMpa()).isEqualTo(testFilm.getMpa());
        assertThat(addedFilm.getGenres()).isEqualTo(testFilm.getGenres());
    }


    @Test
    void testUpdateFilm() {
        Film addedFilm = filmStorage.addNewFilm(testFilm);

        addedFilm.setName("Updated Test Film");
        addedFilm.setDescription("Updated Test Description");
        addedFilm.setReleaseDate(LocalDate.of(2023, 2, 1));
        addedFilm.setDuration(130);

        Film updatedFilm = filmStorage.updateFilm(addedFilm);

        assertThat(updatedFilm.getId()).isEqualTo(addedFilm.getId());
        assertThat(updatedFilm.getName()).isEqualTo(addedFilm.getName());
        assertThat(updatedFilm.getDescription()).isEqualTo(addedFilm.getDescription());
        assertThat(updatedFilm.getReleaseDate()).isEqualTo(addedFilm.getReleaseDate());
        assertThat(updatedFilm.getDuration()).isEqualTo(addedFilm.getDuration());
        assertThat(updatedFilm.getMpa()).isEqualTo(addedFilm.getMpa());
        assertThat(updatedFilm.getGenres()).isEqualTo(addedFilm.getGenres());
    }

    @Test
    void testFindFilmById() {
        Film addedFilm = filmStorage.addNewFilm(testFilm);

        Film foundFilm = filmStorage.findFilmById(addedFilm.getId());

        assertThat(foundFilm).isNotNull();
        assertThat(foundFilm.getId()).isEqualTo(addedFilm.getId());
        assertThat(foundFilm.getName()).isEqualTo(addedFilm.getName());
    }

    @Test
    void testGetAllFilms() {
        filmStorage.addNewFilm(testFilm);
        filmStorage.addNewFilm(testFilm);
        Collection<Film> films = filmStorage.getAllFilms();

        assertTrue(films.size() > 0);
    }

    @Test
    void testGetAllPopFilms() {
        filmStorage.addNewFilm(testFilm);
        filmStorage.addNewFilm(testFilm);
        Collection<Film> films = filmStorage.getAllPopFilms();

        assertTrue(films.size() > 0);
    }
}


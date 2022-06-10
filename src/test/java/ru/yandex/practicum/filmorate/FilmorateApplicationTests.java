package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.controller.UserController;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
    Film film = new Film();
    FilmController fc = new FilmController();
    User user = new User();
    UserController uc = new UserController();

    @Test
    void createUserEmailValidationTest() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> uc.createUser(user));
        user.setEmail("a");
        assertThrows(ValidationException.class, () -> uc.createUser(user));
        user.setEmail("a@mail.ru");
        uc.createUser(user);
        assertTrue(uc.getUsers().containsValue(user));
    }

    @Test
    void createUserLoginValidationTest() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> uc.createUser(user));
        user.setLogin(" ");
        assertThrows(ValidationException.class, () -> uc.createUser(user));
        user.setLogin("a");
        uc.createUser(user);
        assertTrue(uc.getUsers().containsValue(user));
    }

    @Test
    void createUserNameValidationTest() {
        user.setName("");
        uc.createUser(user);
        assertEquals(uc.getUsers().get(user.getId()).getName(), user.getLogin());
    }

    @Test
    void createUserBirthdayValidationTest() {
        user.setBirthday(LocalDate.of(2989, 4, 17));
        assertThrows(ValidationException.class, () -> uc.createUser(user));
        user.setBirthday(LocalDate.of(1989, 4, 17));
        uc.createUser(user);
        assertTrue(uc.getUsers().containsValue(user));
    }

    @Test
    void updateUserEmailValidationTest() {
        uc.createUser(user);
        user.setEmail("");
        assertThrows(ValidationException.class, () -> uc.updateUser(user));
        user.setEmail("a");
        assertThrows(ValidationException.class, () -> uc.updateUser(user));
        user.setEmail("a@mail.ru");
        uc.updateUser(user);
        assertTrue(uc.getUsers().containsValue(user));
    }

    @Test
    void updateUserLoginValidationTest() {
        uc.createUser(user);
        user.setLogin("");
        assertThrows(ValidationException.class, () -> uc.updateUser(user));
        user.setLogin(" ");
        assertThrows(ValidationException.class, () -> uc.updateUser(user));
        user.setLogin("a");
        uc.updateUser(user);
        assertTrue(uc.getUsers().containsValue(user));
    }

    @Test
    void updateUserNameValidationTest() {
        uc.createUser(user);
        user.setName("");
        uc.updateUser(user);
        assertEquals(uc.getUsers().get(user.getId()).getName(), user.getLogin());
    }

    @Test
    void updateUserBirthdayValidationTest() {
        uc.createUser(user);
        user.setBirthday(LocalDate.of(2989, 4, 17));
        assertThrows(ValidationException.class, () -> uc.updateUser(user));
        user.setBirthday(LocalDate.of(1989, 4, 17));
        uc.updateUser(user);
        assertTrue(uc.getUsers().containsValue(user));
    }

    @Test
    void updateUserIdValidationTest() {
        uc.createUser(user);
        user.setId(2);
        assertThrows(ValidationException.class, () -> uc.updateUser(user));
    }

    @Test
    void createFilmNameValidationTest() {
        film.setName("");
        assertThrows(ValidationException.class, () -> fc.createFilm(film));
        film.setName("a");
        fc.createFilm(film);
        assertTrue(fc.getFilms().containsValue(film));
    }

    @Test
    void createFilmDescriptionValidationTest() {
        StringBuilder value = new StringBuilder();
        value.append("a".repeat(201));
        film.setDescription(value.toString());
        assertThrows(ValidationException.class, () -> fc.createFilm(film));
        value = new StringBuilder();
        value.append("a".repeat(200));
        film.setDescription(value.toString());
        fc.createFilm(film);
        assertTrue(fc.getFilms().containsValue(film));
    }

    @Test
    void createFilmReleaseDateValidationTest() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> fc.createFilm(film));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        fc.createFilm(film);
        assertTrue(fc.getFilms().containsValue(film));
    }

    @Test
    void createFilmDurationValidationTest() {
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> fc.createFilm(film));
        film.setDuration(1);
        fc.createFilm(film);
        assertTrue(fc.getFilms().containsValue(film));
    }

    @Test
    void updateFilmNameValidationTest() {
        fc.createFilm(film);
        film.setName("");
        assertThrows(ValidationException.class, () -> fc.updateFilm(film));
        film.setName("a");
        fc.updateFilm(film);
        assertTrue(fc.getFilms().containsValue(film));
    }

    @Test
    void updateFilmDescriptionValidationTest() {
        fc.createFilm(film);
        StringBuilder value = new StringBuilder();
        value.append("a".repeat(201));
        film.setDescription(value.toString());
        assertThrows(ValidationException.class, () -> fc.updateFilm(film));
        value = new StringBuilder();
        value.append("a".repeat(200));
        film.setDescription(value.toString());
        fc.updateFilm(film);
        assertTrue(fc.getFilms().containsValue(film));
    }

    @Test
    void updateFilmReleaseDateValidationTest() {
        fc.createFilm(film);
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> fc.updateFilm(film));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        fc.updateFilm(film);
        assertTrue(fc.getFilms().containsValue(film));
    }

    @Test
    void updateFilmDurationValidationTest() {
        fc.createFilm(film);
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> fc.updateFilm(film));
        film.setDuration(1);
        fc.updateFilm(film);
        assertTrue(fc.getFilms().containsValue(film));
    }

    @Test
    void updateFilmIdValidationTest() {
        fc.createFilm(film);
        film.setId(2);
        assertThrows(ValidationException.class, () -> fc.updateFilm(film));
    }

    @BeforeEach
    void testEnvironment() {
        fc.getFilms().clear();
        uc.getUsers().clear();

        film.setName("A");
        film.setDescription("AA");
        film.setReleaseDate(LocalDate.of(1989, 4, 17));
        film.setDuration(100);

        user.setEmail("a@mail.ru");
        user.setLogin("A");
        user.setName("AA");
        user.setBirthday(LocalDate.of(1989, 4, 17));
    }
}
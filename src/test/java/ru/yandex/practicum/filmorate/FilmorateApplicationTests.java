package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
    Film film = new Film();
    User user = new User();
    UserController uc = new UserController(new UserService(new InMemoryUserStorage()));
    FilmController fc = new FilmController(uc, new FilmService(new InMemoryFilmStorage()));

    @Test
    void createUserEmailValidationTest() {
        user.setEmail("");
        assertThrows(ValidationException.class, () -> uc.create(user));
        user.setEmail("a");
        assertThrows(ValidationException.class, () -> uc.create(user));
        user.setEmail("a@mail.ru");
        uc.create(user);
        assertTrue(uc.getUserService().getUserStorage().getUsers().containsValue(user));
    }

    @Test
    void createUserLoginValidationTest() {
        user.setLogin("");
        assertThrows(ValidationException.class, () -> uc.create(user));
        user.setLogin(" ");
        assertThrows(ValidationException.class, () -> uc.create(user));
        user.setLogin("a");
        uc.create(user);
        assertTrue(uc.getUserService().getUserStorage().getUsers().containsValue(user));
    }

    @Test
    void createUserNameValidationTest() {
        user.setName("");
        uc.create(user);
        assertEquals(uc.getUserService().getUserStorage().getUsers().get(user.getId()).getName(), user.getLogin());
    }

    @Test
    void createUserBirthdayValidationTest() {
        user.setBirthday(LocalDate.of(2989, 4, 17));
        assertThrows(ValidationException.class, () -> uc.create(user));
        user.setBirthday(LocalDate.of(1989, 4, 17));
        uc.create(user);
        assertTrue(uc.getUserService().getUserStorage().getUsers().containsValue(user));
    }

    @Test
    void updateUserEmailValidationTest() {
        uc.create(user);
        user.setEmail("");
        assertThrows(ValidationException.class, () -> uc.update(user));
        user.setEmail("a");
        assertThrows(ValidationException.class, () -> uc.update(user));
        user.setEmail("a@mail.ru");
        uc.update(user);
        assertTrue(uc.getUserService().getUserStorage().getUsers().containsValue(user));
    }

    @Test
    void updateUserLoginValidationTest() {
        uc.create(user);
        user.setLogin("");
        assertThrows(ValidationException.class, () -> uc.update(user));
        user.setLogin(" ");
        assertThrows(ValidationException.class, () -> uc.update(user));
        user.setLogin("a");
        uc.update(user);
        assertTrue(uc.getUserService().getUserStorage().getUsers().containsValue(user));
    }

    @Test
    void updateUserNameValidationTest() {
        uc.create(user);
        user.setName("");
        uc.update(user);
        assertEquals(uc.getUserService().getUserStorage().getUsers().get(user.getId()).getName(), user.getLogin());
    }

    @Test
    void updateUserBirthdayValidationTest() {
        uc.create(user);
        user.setBirthday(LocalDate.of(2989, 4, 17));
        assertThrows(ValidationException.class, () -> uc.update(user));
        user.setBirthday(LocalDate.of(1989, 4, 17));
        uc.update(user);
        assertTrue(uc.getUserService().getUserStorage().getUsers().containsValue(user));
    }

    @Test
    void updateUserIdValidationTest() {
        uc.create(user);
        user.setId(2);
        assertThrows(ValidationException.class, () -> uc.update(user));
    }

    @Test
    void createFilmNameValidationTest() {
        film.setName("");
        assertThrows(ValidationException.class, () -> fc.create(film));
        film.setName("a");
        fc.create(film);
        assertTrue(fc.getFilmService().getFilmStorage().getFilms().containsValue(film));
    }

    @Test
    void createFilmDescriptionValidationTest() {
        StringBuilder value = new StringBuilder();
        value.append("a".repeat(201));
        film.setDescription(value.toString());
        assertThrows(ValidationException.class, () -> fc.create(film));
        value = new StringBuilder();
        value.append("a".repeat(200));
        film.setDescription(value.toString());
        fc.create(film);
        assertTrue(fc.getFilmService().getFilmStorage().getFilms().containsValue(film));
    }

    @Test
    void createFilmReleaseDateValidationTest() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> fc.create(film));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        fc.create(film);
        assertTrue(fc.getFilmService().getFilmStorage().getFilms().containsValue(film));
    }

    @Test
    void createFilmDurationValidationTest() {
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> fc.create(film));
        film.setDuration(1);
        fc.create(film);
        assertTrue(fc.getFilmService().getFilmStorage().getFilms().containsValue(film));
    }

    @Test
    void updateFilmNameValidationTest() {
        fc.create(film);
        film.setName("");
        assertThrows(ValidationException.class, () -> fc.update(film));
        film.setName("a");
        fc.update(film);
        assertTrue(fc.getFilmService().getFilmStorage().getFilms().containsValue(film));
    }

    @Test
    void updateFilmDescriptionValidationTest() {
        fc.create(film);
        StringBuilder value = new StringBuilder();
        value.append("a".repeat(201));
        film.setDescription(value.toString());
        assertThrows(ValidationException.class, () -> fc.update(film));
        value = new StringBuilder();
        value.append("a".repeat(200));
        film.setDescription(value.toString());
        fc.update(film);
        assertTrue(fc.getFilmService().getFilmStorage().getFilms().containsValue(film));
    }

    @Test
    void updateFilmReleaseDateValidationTest() {
        fc.create(film);
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> fc.update(film));
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        fc.update(film);
        assertTrue(fc.getFilmService().getFilmStorage().getFilms().containsValue(film));
    }

    @Test
    void updateFilmDurationValidationTest() {
        fc.create(film);
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> fc.update(film));
        film.setDuration(1);
        fc.update(film);
        assertTrue(fc.getFilmService().getFilmStorage().getFilms().containsValue(film));
    }

    @Test
    void updateFilmIdValidationTest() {
        fc.create(film);
        film.setId(2);
        assertThrows(ValidationException.class, () -> fc.update(film));
    }

    @BeforeEach
    void testEnvironment() {
        fc.getFilmService().getFilmStorage().getFilms().clear();
        uc.getUserService().getUserStorage().getUsers().clear();

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
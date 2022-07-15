package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {
    private final UserStorage us;
    private final FilmStorage fs;
    User user1 = new User();
    User user2 = new User();
    User user3 = new User();

    Film film1 = new Film();
    Film film2 = new Film();
    Film film3 = new Film();

    @Test
    @Order(1)
    public void createUserTest() {
        user1.setEmail("A@mail.ru");
        user1.setLogin("AAA");
        user1.setName("AAAA");
        user1.setBirthday(LocalDate.of(2000, 1, 1));

        User createdUser = us.createUser(user1);
        assertEquals(createdUser, user1);
        assertEquals(createdUser.getId(), 1);
    }

    @Test
    @Order(2)
    public void updateUserTest() {
        user2.setEmail("B@mail.ru");
        user2.setLogin("BBB");
        user2.setName("BBBB");
        user2.setBirthday(LocalDate.of(2000, 1, 1));
        user2.setId(1);

        User updatedUser = us.updateUser(user2);
        assertEquals(updatedUser, user2);
        assertEquals(updatedUser.getId(), 1);
    }

    @Test
    @Order(3)
    public void getUserListTest() {
        user1.setEmail("A@mail.ru");
        user1.setLogin("AAA");
        user1.setName("AAAA");
        user1.setBirthday(LocalDate.of(2000, 1, 1));

        user2.setEmail("B@mail.ru");
        user2.setLogin("BBB");
        user2.setName("BBBB");
        user2.setBirthday(LocalDate.of(2000, 1, 1));
        user2.setId(1);

        us.createUser(user1);
        user1.setId(2);
        assertEquals(user2, us.getUserList().get(0));
        assertEquals(user1, us.getUserList().get(1));
        assertEquals(2, us.getUserList().size());
    }

    @Test
    @Order(4)
    public void getUserByIdTest() {
        user2.setEmail("B@mail.ru");
        user2.setLogin("BBB");
        user2.setName("BBBB");
        user2.setBirthday(LocalDate.of(2000, 1, 1));
        user2.setId(1);
        assertEquals(user2, us.getUserById(1));
    }

    @Test
    @Order(5)
    public void addFriendTest() {
        us.addFriend(1, 2);
        assertTrue(us.getUserById(1).getFriendsSet().contains(2));
    }

    @Test
    @Order(6)
    public void removeFriendTest() {
        us.removeFriend(1, 2);
        assertTrue(us.getUserById(1).getFriendsSet().isEmpty());
    }

    @Test
    @Order(7)
    public void getFriendListTest() {
        us.addFriend(1, 2);
        assertEquals(2, us.getFriendList(1).get(0).getId());
        assertEquals(1, us.getFriendList(1).size());
    }

    @Test
    @Order(8)
    public void getCommonFriendListTest() {
        user3.setEmail("C@mail.ru");
        user3.setLogin("CCC");
        user3.setName("CCCC");
        user3.setBirthday(LocalDate.of(2000, 1, 1));
        us.createUser(user3);
        us.addFriend(3, 2);
        assertEquals(2, us.getCommonFriendList(1, 3).get(0).getId());
        assertEquals(1, us.getCommonFriendList(1, 3).size());
    }

    @Test
    @Order(9)
    public void createFilm() {
        film1.setName("AAA");
        film1.setDescription("AAAA");
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setMpa(new MPA(1, null));

        Film createdFilm = fs.createFilm(film1);
        assertEquals(createdFilm, film1);
        assertEquals(createdFilm.getId(), 1);
    }

    @Test
    @Order(10)
    public void updateFilm() {
        film2.setName("BBB");
        film2.setDescription("BBBB");
        film2.setDuration(100);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));
        film2.setMpa(new MPA(1, null));
        film2.setId(1);

        Film updatedFilm = fs.updateFilm(film2);
        assertEquals(updatedFilm, film2);
        assertEquals(updatedFilm.getId(), 1);
    }

    @Test
    @Order(12)
    public void getFilmsList() {
        film1.setName("AAA");
        film1.setDescription("AAAA");
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setMpa(new MPA(1, null));

        film2.setName("BBB");
        film2.setDescription("BBBB");
        film2.setDuration(100);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));
        film2.setMpa(new MPA(1, null));
        film2.setId(1);

        fs.createFilm(film1);
        film1.setId(2);
        assertEquals(film2, fs.getFilmsList().get(0));
        assertEquals(film1, fs.getFilmsList().get(1));
        assertEquals(2, fs.getFilmsList().size());
    }

    @Test
    @Order(11)
    public void getFilmById() {
        film2.setName("BBB");
        film2.setDescription("BBBB");
        film2.setDuration(100);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));
        film2.setMpa(new MPA(1, null));
        film2.setId(1);

        assertEquals(film2, fs.getFilmById(1));
    }

    @Test
    @Order(13)
    public void putLike() {
        fs.putLike(1, 1);
        assertTrue(fs.getFilmById(1).getLikesSet().contains(1));
    }

    @Test
    @Order(14)
    public void removeLike() {
        fs.removeLike(1, 1);
        assertTrue(fs.getFilmById(1).getLikesSet().isEmpty());
    }

    @Test
    @Order(15)
    public void getPopularFilms() {
        assertEquals(fs.getFilmsList().get(0).getId(), 1);
        fs.putLike(2, 1);
        assertEquals(fs.getPopularFilms(10).get(0).getId(), 2);
    }
}
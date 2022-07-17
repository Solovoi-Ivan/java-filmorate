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
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.filmGenres.FilmWithGenresStorageDB;
import ru.yandex.practicum.filmorate.storage.films.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friendships.FriendshipsDBStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesDBStorage;
import ru.yandex.practicum.filmorate.storage.users.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {
    private final UserDbStorage us;
    private final FilmDbStorage fs;
    private final FilmWithGenresStorageDB fgs;
    private final LikesDBStorage ls;
    private final FriendshipsDBStorage frs;
    private final UserService userService;
    User user1 = new User();
    User user2 = new User();
    User user3 = new User();

    Film film1 = new Film();
    Film film2 = new Film();

    @Test
    @Order(1)
    public void createUserTest() {
        user1.setEmail("A@mail.ru");
        user1.setLogin("AAA");
        user1.setName("AAAA");
        user1.setBirthday(LocalDate.of(2000, 1, 1));

        User createdUser = us.create(user1);
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

        User updatedUser = us.update(user2);
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

        us.create(user1);
        user1.setId(2);
        assertEquals(user2, us.getList().get(0));
        assertEquals(user1, us.getList().get(1));
        assertEquals(2, us.getList().size());
    }

    @Test
    @Order(4)
    public void getUserByIdTest() {
        user2.setEmail("B@mail.ru");
        user2.setLogin("BBB");
        user2.setName("BBBB");
        user2.setBirthday(LocalDate.of(2000, 1, 1));
        user2.setId(1);
        assertEquals(user2, us.getById(1));
    }

    @Test
    @Order(5)
    public void addFriendTest() {
        frs.addFriendToDataBase(1, 2);
        assertTrue(userService.getById(1).getFriendsSet().contains(2));
    }

    @Test
    @Order(6)
    public void removeFriendTest() {
        frs.removeFriendFromDataBase(1, 2);
        assertTrue(userService.getById(1).getFriendsSet().isEmpty());
    }

    @Test
    @Order(7)
    public void getFriendListTest() {
        frs.addFriendToDataBase(1, 2);


        assertEquals(2, userService.getFriendList(1).get(0).getId());
        assertEquals(1, userService.getFriendList(1).size());
    }

    @Test
    @Order(8)
    public void getCommonFriendListTest() {
        user3.setEmail("C@mail.ru");
        user3.setLogin("CCC");
        user3.setName("CCCC");
        user3.setBirthday(LocalDate.of(2000, 1, 1));
        us.create(user3);
        frs.addFriendToDataBase(3, 2);
        assertEquals(2, userService.getCommonFriendList(1, 3).get(0).getId());
        assertEquals(1, userService.getCommonFriendList(1, 3).size());
    }

    @Test
    @Order(9)
    public void createFilm() {
        film1.setName("AAA");
        film1.setDescription("AAAA");
        film1.setDuration(100);
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setMpa(new MPA(1, null));

        Film createdFilm = fgs.genreIdToDataBase(fs.create(film1));
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

        Film updatedFilm = fgs.genreIdToDataBase(fs.update(film2));
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

        fgs.genreIdToDataBase(fs.create(film1));
        film1.setId(2);
        assertEquals(film2, fs.getList().get(0));
        assertEquals(film1, fs.getList().get(1));
        assertEquals(2, fs.getList().size());
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

        assertEquals(film2, fs.getById(1));
    }

    @Test
    @Order(13)
    public void putLike() {
        ls.putLike(1, 1);
        assertTrue(fs.getById(1).getLikesSet().contains(1));
    }

    @Test
    @Order(14)
    public void removeLike() {
        ls.removeLike(1, 1);
        assertTrue(fs.getById(1).getLikesSet().isEmpty());
    }

    @Test
    @Order(15)
    public void getPopularFilms() {
        assertEquals(fs.getList().get(0).getId(), 1);
        ls.putLike(2, 1);
        assertEquals(fs.getPopular(10).get(0).getId(), 2);
    }
}
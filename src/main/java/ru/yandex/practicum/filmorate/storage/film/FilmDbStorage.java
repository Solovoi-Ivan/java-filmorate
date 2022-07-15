package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilmsList() {
        String sqlQuery = "select * from FILMS";
        return addLikesInfo(addGenreId(jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRowToFilm)));
    }

    @Override
    public Film getFilmById(int id) {
        String sqlQuery = "select * from FILMS where film_id = ?";
        return addLikesInfo(addGenreId(jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRowToFilm, id))).get(0);
    }

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "insert into FILMS (film_name, description, release_date, duration, mpa_rating_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        if (!film.getGenres().isEmpty()) {
            genreToMapRow(film);
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update FILMS set " +
                "film_name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_rating_id = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        String sqlQueryLikes = "delete from USER_FILM_LIKES where film_id = ?";
        jdbcTemplate.update(sqlQueryLikes, film.getId());
        String sqlQueryForGenre = "delete from FILM_GENRES where film_id = ?";
        jdbcTemplate.update(sqlQueryForGenre, film.getId());
        if (!film.getGenres().isEmpty()) {
            genreToMapRow(film);
        }
        return film;
    }

    @Override
    public void putLike(int filmId, int userId) {
        String sqlQuery = "insert into USER_FILM_LIKES (film_id, user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sqlQuery = "delete from USER_FILM_LIKES where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> filmList = getFilmsList();
        Comparator<Film> comparator = (f1, f2) -> f2.getLikesSet().size() - f1.getLikesSet().size();
        filmList.sort(comparator);
        if (filmList.size() > count) {
            filmList.subList(count, filmList.size()).clear();
        }
        return filmList;
    }

    @Override
    public Map<Integer, Film> getFilmsMap() {
        Map<Integer, Film> filmsMap = new HashMap<>();
        for (Film film : getFilmsList()) {
            filmsMap.put(film.getId(), film);
        }
        return filmsMap;
    }

    static Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("film_name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(new MPA());
        film.getMpa().setId(rs.getInt("mpa_rating_id"));
        return film;
    }

    static Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_type"));
        return genre;
    }

    public void genreToMapRow(Film film) {
        String sqlQuery = "insert into FILM_GENRES (film_id, genre_id) " +
                "values (?, ?)";
        for (Genre genre : new ArrayList<>(film.getGenres())) {
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    public List<Film> addGenreId(List<Film> films) {
        for (Film film : films) {
            String sqlQuery = "select fg.film_id, fg.genre_id, g.genre_type " +
                    "from FILM_GENRES AS fg join GENRES as g on fg.genre_id = g.genre_id where film_id = ?";
            film.getGenres().addAll(jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRowToGenre, film.getId()));
        }
        return films;
    }

    public List<Film> addLikesInfo(List<Film> films) {
        for (Film film : films) {
            String sqlQuery = "select film_id, user_id from USER_FILM_LIKES where film_id = ?";
            film.getLikesSet().addAll(jdbcTemplate.query(sqlQuery, this::mapRowToUserId, film.getId()));
        }
        return films;
    }

    public int mapRowToUserId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("user_id");
    }
}
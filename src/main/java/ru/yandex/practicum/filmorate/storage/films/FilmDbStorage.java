package ru.yandex.practicum.filmorate.storage.films;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
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
    public List<Film> getList() {
        String sqlQuery = "select * from FILMS";
        return addLikesInfo(jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRowToFilm));
    }

    @Override
    public List<Film> getById(int id) {
        String sqlQuery = "select * from FILMS where film_id = ?";
        return addLikesInfo(jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRowToFilm, id));
    }

    @Override
    public Film create(Film film) {
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
        return film;
    }

    @Override
    public Film update(Film film) {
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

        return film;
    }

    @Override
    public List<Film> getPopular(int count) {
        List<Film> filmList = getList();
        Comparator<Film> comparator = (f1, f2) -> f2.getLikesSet().size() - f1.getLikesSet().size();
        filmList.sort(comparator);
        if (filmList.size() > count) {
            filmList.subList(count, filmList.size()).clear();
        }
        return filmList;
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
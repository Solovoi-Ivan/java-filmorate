package ru.yandex.practicum.filmorate.storage.filmGenres;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class FilmWithGenresStorageDB implements FilmWithGenresStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmWithGenresStorageDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film genreIdToDataBase(Film film) {
        String sqlQueryForGenre = "delete from FILM_GENRES where film_id = ?";
        jdbcTemplate.update(sqlQueryForGenre, film.getId());

        if (!film.getGenres().isEmpty()) {
            genreToMapRow(film);
        }

        return film;
    }

    @Override
    public List<Film> genreIdFromDataBase(List<Film> films) {
        for (Film film : films) {
            String sqlQuery = "select fg.film_id, fg.genre_id, g.genre_type " +
                    "from FILM_GENRES AS fg join GENRES as g on fg.genre_id = g.genre_id where film_id = ?";
            film.getGenres().addAll(jdbcTemplate.query(sqlQuery, FilmWithGenresStorageDB::mapRowToGenre, film.getId()));
        }
        return films;
    }

    public void genreToMapRow(Film film) {
        String sqlQuery = "insert into FILM_GENRES (film_id, genre_id) " +
                "values (?, ?)";
        for (Genre genre : new ArrayList<>(film.getGenres())) {
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    static Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_type"));
        return genre;
    }
}

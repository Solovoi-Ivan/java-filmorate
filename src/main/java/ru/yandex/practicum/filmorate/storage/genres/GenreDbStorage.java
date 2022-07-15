package ru.yandex.practicum.filmorate.storage.genres;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
@Primary
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, Genre> getGenresMap() {
        String sqlQuery = "select * from GENRES";
        Map<Integer, Genre> genres = new HashMap<>();;
        for (Genre genre : jdbcTemplate.query(sqlQuery, GenreDbStorage::mapRowToGenre)) {
            genres.put(genre.getId(), genre);
        }
        return genres;
    }

    static Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_type"));
        return genre;
    }
}
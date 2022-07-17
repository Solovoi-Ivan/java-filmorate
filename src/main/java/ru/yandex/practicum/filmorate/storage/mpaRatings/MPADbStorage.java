package ru.yandex.practicum.filmorate.storage.mpaRatings;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
@Primary
public class MPADbStorage implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;

    public MPADbStorage (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, MPA> getMpaRatingMap() {
        String sqlQuery = "select * from MPA_RATINGS";
        Map<Integer, MPA> mpaRating = new HashMap<>();
        for (MPA mpa : jdbcTemplate.query(sqlQuery, MPADbStorage::mapRowToMPA)) {
            mpaRating.put(mpa.getId(), mpa);
        }
        return mpaRating;
    }

    static MPA mapRowToMPA(ResultSet rs, int rowNum) throws SQLException {
        MPA mpa = new MPA();
        mpa.setId(rs.getInt("mpa_rating_id"));
        mpa.setName(rs.getString("mpa_rating_description"));
        return mpa;
    }
}
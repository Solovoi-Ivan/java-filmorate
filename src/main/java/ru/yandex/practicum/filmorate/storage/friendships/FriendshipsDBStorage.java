package ru.yandex.practicum.filmorate.storage.friendships;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@Primary
public class FriendshipsDBStorage implements FriendshipsStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendshipsDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriendToDB(int userId, int friendId) {
        String sqlQuery = "insert into FRIENDSHIPS (first_user_id, second_user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriendFromDB(int userId, int friendId) {
        String sqlQuery = "delete from FRIENDSHIPS where first_user_id = ? and second_user_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> addFriendsInfoFromDBToList(List<User> list) {
        String sqlQuery = "select second_user_id from FRIENDSHIPS where first_user_id = ?";
        for (User user : list) {
            user.getFriendsSet().addAll(jdbcTemplate.query(sqlQuery, this::mapRowToFriendId, user.getId()));
        }
        return list;
    }

    @Override
    public User clearFriendsList(User user) {
        String sqlQueryFriend = "delete from FRIENDSHIPS where first_user_id = ?";
        jdbcTemplate.update(sqlQueryFriend, user.getId());
        String sqlQueryFriendOf = "delete from FRIENDSHIPS where second_user_id = ?";
        jdbcTemplate.update(sqlQueryFriendOf, user.getId());
        return user;
    }

    public int mapRowToFriendId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("second_user_id");
    }
}

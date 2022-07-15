package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUserList() {
        String sqlQuery = "select * from USERS";
        return addUserFriendsInfo(jdbcTemplate.query(sqlQuery, UserDbStorage::mapRowToUser));
    }

    @Override
    public User getUserById(int userId) {
        String sqlQuery = "select * from USERS where user_id = ?";
        return addUserFriendsInfo(jdbcTemplate.query(sqlQuery, UserDbStorage::mapRowToUser, userId)).get(0);
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into USERS (email, login, user_name, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update USERS set " +
                "email = ?, " +
                "login = ?, " +
                "user_name = ?, " +
                "birthday = ? " +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), user.getId());

        String sqlQueryFriend = "delete from FRIENDSHIPS where first_user_id = ?";
        jdbcTemplate.update(sqlQueryFriend, user.getId());
        String sqlQueryFriendOf = "delete from FRIENDSHIPS where second_user_id = ?";
        jdbcTemplate.update(sqlQueryFriendOf, user.getId());
        String sqlQueryLikes = "delete from USER_FILM_LIKES where user_id = ?";
        jdbcTemplate.update(sqlQueryLikes, user.getId());
        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "insert into FRIENDSHIPS (first_user_id, second_user_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sqlQuery = "delete from FRIENDSHIPS where first_user_id = ? and second_user_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getFriendList(int userId){
        List<User> friendList = new ArrayList<>();
        for (int id : getUserById(userId).getFriendsSet()) {
            friendList.add(getUserById(id));
        }
        return friendList;
    }

    @Override
    public List<User> getCommonFriendList(int firstId, int secondId) {
        List<User> list = new ArrayList<>();
        for (User user : getFriendList(firstId)) {
            if (getFriendList(secondId).contains(user)) {
                list.add(user);
            }
        }
        return list;
    }

    @Override
    public Map<Integer, User> getUsersMap() {
        Map<Integer, User> usersMap = new HashMap<>();
        for (User user : getUserList()) {
            usersMap.put(user.getId(), user);
        }
        return usersMap;
    }

    static User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("user_name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }

    public List<User> addUserFriendsInfo(List<User> list) {
        String sqlQuery = "select second_user_id from FRIENDSHIPS where first_user_id = ?";
        for (User user : list) {
            user.getFriendsSet().addAll(jdbcTemplate.query(sqlQuery, this::mapRowToFriendId, user.getId()));
        }
        return list;
    }

    public int mapRowToFriendId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("second_user_id");
    }
}

package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    public List<User> getUserList();

    public User getUserById(int userId);

    public User createUser(User user);

    public User updateUser(User user);

    public void addFriend(int userId, int friendId);

    public void removeFriend(int userId, int friendId);

    public List<User> getFriendList(int userId);

    public List<User> getCommonFriendList(int firstId, int secondId);

    public Map<Integer, User> getUsersMap();
}
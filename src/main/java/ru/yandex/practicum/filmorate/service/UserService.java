package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getUserList() {
        return userStorage.getUserList();
    }

    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriendList(int userId) {
        return userStorage.getFriendList(userId);
    }

    public List<User> getCommonFriendList(int firstId, int secondId) {
        return userStorage.getCommonFriendList(firstId, secondId);
    }

    public Map<Integer, User> getUsersMap() {
        return userStorage.getUsersMap();
    }
}

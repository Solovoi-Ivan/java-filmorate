package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private int userId = 0;
    Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int userId) {
        return users.get(userId);
    }

    @Override
    public User createUser(User user) {
        userId++;
        user.setId(userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        users.get(userId).getFriendsSet().add(friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        users.get(userId).getFriendsSet().remove(friendId);
    }

    @Override
    public List<User> getFriendList(int userId) {
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
        return users;
    }
}
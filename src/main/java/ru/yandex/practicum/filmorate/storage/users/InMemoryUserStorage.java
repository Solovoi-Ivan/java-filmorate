package ru.yandex.practicum.filmorate.storage.users;

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
    public List<User> getList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(int userId) {
        return users.get(userId);
    }

    @Override
    public User create(User user) {
        userId++;
        user.setId(userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void addFriend(int userId, int friendId) {
        users.get(userId).getFriendsSet().add(friendId);
    }

    public void removeFriend(int userId, int friendId) {
        users.get(userId).getFriendsSet().remove(friendId);
    }

    public List<User> getFriendList(int userId) {
        List<User> friendList = new ArrayList<>();
        for (int id : getById(userId).getFriendsSet()) {
            friendList.add(getById(id));
        }
        return friendList;
    }

    public List<User> getCommonFriendList(int firstId, int secondId) {
        List<User> list = new ArrayList<>();
        for (User user : getFriendList(firstId)) {
            if (getFriendList(secondId).contains(user)) {
                list.add(user);
            }
        }
        return list;
    }

    public Map<Integer, User> getUsersMap() {
        return users;
    }
}
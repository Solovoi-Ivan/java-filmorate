package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendships.FriendshipsStorage;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipsStorage friendshipsStorage;

    public List<User> getList() {
        return friendshipsStorage.addFriendsInfoFromDBToList(userStorage.getList());
    }

    public User getById(int userId) {
        return friendshipsStorage.addFriendsInfoFromDBToList(userStorage.getById(userId)).get(0);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return friendshipsStorage.clearFriendsList(userStorage.update(user));
    }

    public void addFriend(int userId, int friendId) {
        friendshipsStorage.addFriendToDB(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        friendshipsStorage.removeFriendFromDB(userId, friendId);
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

    public Map<Integer, User> getMap() {
        Map<Integer, User> usersMap = new HashMap<>();
        for (User user : getList()) {
            usersMap.put(user.getId(), user);
        }
        return usersMap;
    }
}

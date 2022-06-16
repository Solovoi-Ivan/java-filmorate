package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(int userId, int friendId) {
        userStorage.getUsers().get(userId).getFriendsSet().add(friendId);
        userStorage.getUsers().get(friendId).getFriendsSet().add(userId);
    }

    public void removeFriend(int userId, int friendId) {
        userStorage.getUsers().get(userId).getFriendsSet().remove(friendId);
        userStorage.getUsers().get(friendId).getFriendsSet().remove(userId);
    }

    public List<User> getFriendList(int userId) {
        List<User> friendList = new ArrayList<>();
        for (int id : userStorage.getUsers().get(userId).getFriendsSet()) {
            friendList.add(userStorage.getUsers().get(id));
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

    public UserStorage getUserStorage() {
        return userStorage;
    }
}

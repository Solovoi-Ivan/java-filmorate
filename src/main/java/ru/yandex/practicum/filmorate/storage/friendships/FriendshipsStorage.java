package ru.yandex.practicum.filmorate.storage.friendships;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface FriendshipsStorage {
    public void addFriendToDataBase(int userId, int friendId);

    public void removeFriendFromDataBase(int userId, int friendId);

    public List<User> addFriendsInfoFromDataBaseToList(List<User> list);

    public User clearFriendsList(User user);
}

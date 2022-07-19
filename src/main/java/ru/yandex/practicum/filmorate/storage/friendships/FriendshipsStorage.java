package ru.yandex.practicum.filmorate.storage.friendships;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipsStorage {
    public void addFriendToDB(int userId, int friendId);

    public void removeFriendFromDB(int userId, int friendId);

    public List<User> addFriendsInfoFromDBToList(List<User> list);

    public User clearFriendsList(User user);
}

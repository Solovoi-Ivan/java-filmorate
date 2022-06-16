package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    public List<User> getUserList();

    public void createUser(User user);

    public void updateUser(User user);

    public Map<Integer, User> getUsers();
}
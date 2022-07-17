package ru.yandex.practicum.filmorate.storage.users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public List<User> getList();

    public User getById(int userId);

    public User create(User user);

    public User update(User user);
}
package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private int userId = 0;
    Map<Integer, User> users = new HashMap<>();
    @Override
    public List<User> getUserList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void createUser(User user) {
        userId++;
        user.setId(userId);
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }
}
package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage { // убрал лишние суффиксы
    public List<User> getList();

    public void create(User user);

    public void update(User user);

    public void delete(int id); // добавил метод удаления

    public Map<Integer, User> getUsers();
}
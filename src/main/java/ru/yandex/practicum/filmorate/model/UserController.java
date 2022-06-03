package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> getUserList() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) {
        user = userValidation(user, "create");
        userId++;
        user.setId(userId);
        users.put(user.getId(), user);
        log.info("POST запрос обработан успешно");
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        user = userValidation(user, "update");
        users.put(user.getId(), user);
        log.info("PUT запрос обработан успешно");
        return user;
    }

    public User userValidation(User user, String task) {
        if (task.equals("update") && !users.containsKey(user.getId())) {
            log.warn("Обновление записи невозможно - пользователя с таким id нет");
            throw new ValidationException();
        } else if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Неправильный email");
            throw new ValidationException();
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Неправильный логин");
            throw new ValidationException();
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Неправильная дата рождения");
            throw new ValidationException();
        } else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            return user;
        } else {
            return user;
        }
    }

    public Map<Integer, User> getUsers() {
        return users;
    }
}
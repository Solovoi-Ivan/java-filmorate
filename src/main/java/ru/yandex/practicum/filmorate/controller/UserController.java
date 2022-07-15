package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public List<User> getUserList() {
        return userService.getUserList();
    }

    @GetMapping("/users/{userId}")
    public User getUserById(@PathVariable int userId) {
        String text;
        if (!userService.getUsersMap().containsKey(userId)) {
            text = "Пользователь не найден";
            log.warn(text);
            throw new DataNotFoundException(text);
        } else {
            return userService.getUserById(userId);
        }
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        user = userService.createUser(userStorageValidation(user, "POST"));
        log.info("POST запрос обработан успешно");
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        user = userService.updateUser(userStorageValidation(user, "PUT"));
        log.info("PUT запрос обработан успешно");
        return user;
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) {
        userServiceValidation(userId, friendId, "PUT");
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable int userId, @PathVariable int friendId) {
        userServiceValidation(userId, friendId, "DELETE");
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/users/{userId}/friends")
    public List<User> getFriendList(@PathVariable int userId) {
        userServiceValidation(userId, 0, "GET");
        return userService.getFriendList(userId);
    }

    @GetMapping("/users/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriendList(@PathVariable int userId, @PathVariable int otherId) {
        userServiceValidation(userId, otherId, "GET");
        return userService.getCommonFriendList(userId, otherId);
    }

    public User userStorageValidation(User user, String task) {
        String text;
        if (task.equals("PUT") && !userService.getUsersMap().containsKey(user.getId())) {
            text = "Обновление записи невозможно - пользователя с таким id нет";
            log.warn(text);
            throw new DataNotFoundException(text);
        } else if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            text = "Неправильный email";
            log.warn(text);
            throw new ValidationException(text);
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            text = "Неправильный логин";
            log.warn(text);
            throw new ValidationException(text);
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            text = "Неправильная дата рождения";
            log.warn(text);
            throw new ValidationException(text);
        } else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            return user;
        } else {
            return user;
        }
    }

    public void userServiceValidation(int firstId, int secondId, String task) {
        String text;
        Map<Integer, User> users = userService.getUsersMap();
        if (!users.containsKey(firstId) || secondId != 0 && !users.containsKey(secondId)) {
            text = "Пользователь не найден";
            log.warn(text);
            throw new DataNotFoundException(text);
        } else if (task.equals("PUT") && users.get(firstId).getFriendsSet().contains(secondId)) {
            text = "Пользователь уже в есть в списке друзей";
            log.warn(text);
            throw new ValidationException(text);
        } else if (task.equals("DELETE") && !users.get(firstId).getFriendsSet().contains(secondId)) {
            text = "Пользователь не найден в списке друзей";
            log.warn(text);
            throw new DataNotFoundException(text);
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleFailedValidation(final ValidationException e) {
        return Map.of("error", "NOT VALID", "message", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final DataNotFoundException e) {
        return Map.of("error", "NOT FOUND", "message", e.getMessage());
    }

    public UserService getUserService() {
        return userService;
    }
}
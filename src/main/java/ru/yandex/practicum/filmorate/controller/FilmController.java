package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RestController
@RequiredArgsConstructor
public class FilmController {
    private final UserController userController;
    private final FilmService filmService;
    private final LocalDate releaseDateLimit = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public List<Film> getList() {
        return filmService.getList();
    }

    @GetMapping("/films/{filmId}")
    public Film getById(@PathVariable int filmId) {
        String text;
        if (!filmService.getMap().containsKey(filmId)) {
            text = "Фильм не найден";
            log.warn(text);
            throw new DataNotFoundException(text);
        } else {
            return filmService.getById(filmId);
        }
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        film = filmService.create(filmStorageValidation(film, "create"));
        log.info("POST запрос обработан успешно");
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        film = filmService.update(filmStorageValidation(film, "update"));
        log.info("PUT запрос обработан успешно");
        return film;
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public String putLike(@PathVariable int filmId, @PathVariable int userId) {
        filmServiceValidation(filmId, userId);
        filmService.putLike(filmId, userId);
        return "Лайк успешно добавлен";
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public String removeLike(@PathVariable int filmId, @PathVariable int userId) {
        filmServiceValidation(filmId, userId);
        filmService.removeLike(filmId, userId);
        return "Лайк успешно удален";
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(required = false) String count) {
        String text;
        if (filmService.getMap().isEmpty()) {
            text = "Фильмы не найдены";
            log.warn(text);
            throw new DataNotFoundException(text);
        }
        if (count != null) {
            return filmService.getPopular(Integer.parseInt(count));
        } else {
            return filmService.getPopular(10);
        }
    }

    @GetMapping("/genres")
    public List<Genre> getGenresList() {
        return new ArrayList<>(filmService.getGenresMap().values());
    }

    @GetMapping("/genres/{id}")
    public Genre getGenres(@PathVariable int id) {
        String text;
        if (!filmService.getGenresMap().containsKey(id)) {
            text = "Жанр не найден";
            log.warn(text);
            throw new DataNotFoundException(text);
        } else {
            return filmService.getGenresMap().get(id);
        }
    }

    @GetMapping("/mpa")
    public List<MPA> getMPAList() {
        return new ArrayList<>(filmService.getMpaRatingMap().values());
    }

    @GetMapping("/mpa/{id}")
    public MPA getMPA(@PathVariable int id) {
        String text;
        if (!filmService.getMpaRatingMap().containsKey(id)) {
            text = "МРА рейтинг не найден";
            log.warn(text);
            throw new DataNotFoundException(text);
        } else {
            return filmService.getMpaRatingMap().get(id);
        }
    }

    public Film filmStorageValidation(Film film, String task) {
        String text;
        if (task.equals("update") && !filmService.getMap().containsKey(film.getId())) {
            text = "Обновление записи невозможно - фильма с таким id нет";
            log.warn(text);
            throw new DataNotFoundException(text);
        } else if (film.getName().isEmpty()) {
            text = "Неправильное название фильма";
            log.warn(text);
            throw new ValidationException(text);
        } else if (film.getDescription().length() > 200) {
            text = "Слишком длинное описание фильма";
            log.warn(text);
            throw new ValidationException(text);
        } else if (film.getReleaseDate().isBefore(releaseDateLimit)) {
            text = "Дата выхода фильма слишком ранняя";
            log.warn(text);
            throw new ValidationException(text);
        } else if (film.getDuration() <= 0) {
            text = "Длительность фильма не может быть отрицательной";
            log.warn(text);
            throw new ValidationException(text);
        } else {
            return film;
        }
    }

    public void filmServiceValidation(int filmId, int userId) {
        String text;
        if (!filmService.getMap().containsKey(filmId)) {
            text = "Фильм не найден";
            log.warn(text);
            throw new DataNotFoundException(text);
        }
        if (!userController.getUserService().getMap().containsKey(userId)) {
            text = "Пользователь не найден";
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
}
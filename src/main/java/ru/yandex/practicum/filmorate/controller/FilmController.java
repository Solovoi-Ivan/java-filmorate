package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RestController
@RequiredArgsConstructor
public class FilmController {
    private final UserController userController;
    private final FilmService filmService;
    private LocalDate releaseDateLimit = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public List<Film> getFilmsList() {
        return filmService.getFilmStorage().getFilmsList();
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        filmService.getFilmStorage().createFilm(filmStorageValidation(film, "create"));
        log.info("POST запрос обработан успешно");
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        filmService.getFilmStorage().updateFilm(filmStorageValidation(film, "update"));
        log.info("PUT запрос обработан успешно");
        return film;
    }

    @GetMapping("/films/{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        String text;
        if (!filmService.getFilmStorage().getFilms().containsKey(filmId)) {
            text = "Фильм не найден";
            log.warn(text);
            throw new DataNotFoundException(text);
        } else {
            return filmService.getFilmStorage().getFilms().get(filmId);
        }
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
    public List<Film> getPopularFilms(@RequestParam(required = false) String count) {
        String text;
        if (filmService.getFilmStorage().getFilms().isEmpty()) {
            text = "Фильмы не найдены";
            log.warn(text);
            throw new DataNotFoundException(text);
        }
        if (count != null) {
            return filmService.getPopularFilms(Integer.parseInt(count));
        } else {
            return filmService.getPopularFilms(10);
        }
    }

    public Film filmStorageValidation(Film film, String task) {
        String text;
        if (task.equals("update") && !filmService.getFilmStorage().getFilms().containsKey(film.getId())) {
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
        if (!filmService.getFilmStorage().getFilms().containsKey(filmId)) {
            text = "Фильм не найден";
            log.warn(text);
            throw new DataNotFoundException(text);
        }
        if (!userController.getUserService().getUserStorage().getUsers().containsKey(userId)) {
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

    public FilmService getFilmService() {
        return filmService;
    }

    public LocalDate getReleaseDateLimit() {
        return releaseDateLimit;
    }

    public void setReleaseDateLimit(LocalDate releaseDateLimit) {
        this.releaseDateLimit = releaseDateLimit;
    }
}
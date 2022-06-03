package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.Month.*;

@Slf4j
@RestController
public class FilmController {
    int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    LocalDate releaseDateLimit = LocalDate.of(1895, DECEMBER, 28);

    @GetMapping("/films")
    public List<Film> getFilmList() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) {
        film = filmValidation(film, "create");
        filmId++;
        film.setId(filmId);
        films.put(filmId, film);
        log.info("POST запрос обработан успешно");
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        film = filmValidation(film, "update");
        films.put(film.getId(), film);
        log.info("PUT запрос обработан успешно");
        return film;
    }

    public Film filmValidation(Film film, String task) {
        if (task.equals("update") && !films.containsKey(film.getId())) {
            log.warn("Обновление записи невозможно - фильма с таким id нет");
            throw new ValidationException();
        } else if (film.getName().isEmpty()) {
            log.warn("Неправильное название фильма");
            throw new ValidationException();
        } else if (film.getDescription().length() > 200) {
            log.warn("Слишком длинное описание фильма");
            throw new ValidationException();
        } else if (film.getReleaseDate().isBefore(releaseDateLimit)) {
            log.warn("Дата выхода фильма слишком ранняя");
            throw new ValidationException();
        } else if (film.getDuration() <= 0) {
            log.warn("Длительность фильма не может быть отрицательной");
            throw new ValidationException();
        } else {
            return film;
        }
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }
}

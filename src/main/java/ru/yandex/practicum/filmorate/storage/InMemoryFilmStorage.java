package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    @Override
    public List<Film> getList() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void create(Film film) {
        filmId++;
        film.setId(filmId);
        films.put(filmId, film);
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void delete(int id) {
        films.remove(id);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }
}
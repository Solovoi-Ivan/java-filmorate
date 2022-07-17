package ru.yandex.practicum.filmorate.storage.films;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getList() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        filmId++;
        film.setId(filmId);
        films.put(filmId, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public List<Film> getPopular(int count) {
        List<Film> filmList = getList();
        Comparator<Film> comparator = (f1, f2) -> f2.getLikesSet().size() - f1.getLikesSet().size();
        filmList.sort(comparator);
        if (filmList.size() > count) {
            filmList.subList(count, filmList.size()).clear();
        }
        return filmList;
    }

    public Map<Integer, Film> getFilmsMap() {
        return films;
    }
}
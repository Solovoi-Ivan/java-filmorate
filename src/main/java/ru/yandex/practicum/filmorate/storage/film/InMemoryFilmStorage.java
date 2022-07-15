package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilmsList() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public Film createFilm(Film film) {
        filmId++;
        film.setId(filmId);
        films.put(filmId, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public void putLike(int filmId, int userId) {
        films.get(filmId).getLikesSet().add(userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        films.get(filmId).getLikesSet().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> filmList = getFilmsList();
        Comparator<Film> comparator = (f1, f2) -> f2.getLikesSet().size() - f1.getLikesSet().size();
        filmList.sort(comparator);
        if (filmList.size() > count) {
            filmList.subList(count, filmList.size()).clear();
        }
        return filmList;
    }

    @Override
    public Map<Integer, Film> getFilmsMap() {
        return films;
    }
}
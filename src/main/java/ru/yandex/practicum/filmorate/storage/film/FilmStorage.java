package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    public List<Film> getFilmsList();

    public Film getFilmById(int id);

    public Film createFilm(Film film);

    public Film updateFilm(Film film);

    public void putLike(int filmId, int userId);

    public void removeLike(int filmId, int userId);

    public List<Film> getPopularFilms(int count);

    public Map<Integer, Film> getFilmsMap();
}
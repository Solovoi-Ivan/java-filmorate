package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    public List<Film> getFilmsList();

    public void createFilm(Film film);

    public void updateFilm(Film film);

    public Map<Integer, Film> getFilms();
}
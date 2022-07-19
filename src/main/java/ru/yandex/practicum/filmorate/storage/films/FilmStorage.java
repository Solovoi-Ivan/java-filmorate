package ru.yandex.practicum.filmorate.storage.films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public List<Film> getList();

    public List<Film> getById(int id);

    public Film create(Film film);

    public Film update(Film film);

    public List<Film> getPopular(int count);
}
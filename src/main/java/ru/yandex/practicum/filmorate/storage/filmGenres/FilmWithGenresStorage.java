package ru.yandex.practicum.filmorate.storage.filmGenres;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmWithGenresStorage {
    public Film genreIdToDataBase(Film film);

    public List<Film> genreIdFromDataBase(List<Film> films);
}

package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage { // убрал лишние суффиксы
    public List<Film> getList();

    public void create(Film film);

    public void update(Film film);

    public void delete(int id); // добавил метод удаления

    public Map<Integer, Film> getFilms();
}
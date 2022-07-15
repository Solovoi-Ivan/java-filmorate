package ru.yandex.practicum.filmorate.storage.genres;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashMap;
import java.util.Map;

//@Repository
public class InMemoryGenresStorage {
    private final Map<Integer, Genre> genres = new HashMap<>();;

    public InMemoryGenresStorage() {
        genres.put(1, new Genre(1, "Комедия"));
        genres.put(2, new Genre(2, "Драма"));
        genres.put(3, new Genre(3, "Мультфильм"));
        genres.put(4, new Genre(4, "Триллер"));
        genres.put(5, new Genre(5, "Документальный"));
        genres.put(6, new Genre(6, "Боевик"));
    }

    public Map<Integer, Genre> getGenresMap() {
        return genres;
    }
}
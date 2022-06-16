package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public void putLike(int filmId, int userId) {
        filmStorage.getFilms().get(filmId).getLikesSet().add(userId);
    }

    public void removeLike(int filmId, int userId) {
        filmStorage.getFilms().get(filmId).getLikesSet().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> filmList = filmStorage.getFilmsList();
        Comparator<Film> comparator = (f1, f2) -> f2.getLikesSet().size() - f1.getLikesSet().size();
        filmList.sort(comparator);
        if (filmList.size() > count) {
            filmList.subList(count, filmList.size()).clear();
        }
        return filmList;
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }
}

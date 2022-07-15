package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final MPAStorage mpaStorage;
    private final GenreDbStorage genresStorage;

    public List<Film> getFilmsList() {
        List<Film> list = new ArrayList<>();
        for (Film film : filmStorage.getFilmsList()) {
            list.add(addInfoFromGenreAndMPAStorages(film));
        }
        return list;
    }

    public Film getFilmById(int id) {
        return addInfoFromGenreAndMPAStorages(filmStorage.getFilmById(id));
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(addInfoFromGenreAndMPAStorages(film));
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(addInfoFromGenreAndMPAStorages(film));
    }

    public void putLike(int filmId, int userId) {
        filmStorage.putLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public Map<Integer, Genre> getGenresMap() {
        return genresStorage.getGenresMap();
    }

    public Map<Integer, MPA> getMpaRatingMap() {
        return mpaStorage.getMpaRatingMap();
    }

    public Map<Integer, Film> getFilmsMap() {
        return filmStorage.getFilmsMap();
    }

    public Film addInfoFromGenreAndMPAStorages(Film film) {
        film.setMpa(getMpaRatingMap().get(film.getMpa().getId()));
        for (Genre genre : film.getGenres()) {
            genre.setName(getGenresMap().get(genre.getId()).getName());
        }
        return film;
    }
}

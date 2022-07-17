package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.filmGenres.FilmWithGenresStorage;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.mpaRatings.MPAStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final MPAStorage mpaStorage;
    private final GenreDbStorage genresStorage;
    private final LikesStorage likesStorage;

    private final FilmWithGenresStorage filmWithGenresStorage;

    public List<Film> getList() {
        List<Film> list = new ArrayList<>();
        for (Film film : filmWithGenresStorage.genreIdFromDataBase(filmStorage.getList())) {
            list.add(addInfoFromGenreAndMPAStorages(film));
        }
        return list;
    }

    public Film getById(int id) {
        List<Film> list = new ArrayList<>();
        list.add(filmStorage.getById(id));
        return addInfoFromGenreAndMPAStorages(filmWithGenresStorage.genreIdFromDataBase(list).get(0));
    }

    public Film create(Film film) {
        return filmWithGenresStorage.genreIdToDataBase(filmStorage.create(addInfoFromGenreAndMPAStorages(film)));
    }

    public Film update(Film film) {
        return filmWithGenresStorage.genreIdToDataBase(filmStorage.update(addInfoFromGenreAndMPAStorages(film)));
    }

    public void putLike(int filmId, int userId) {
        likesStorage.putLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        likesStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    public Map<Integer, Genre> getGenresMap() {
        return genresStorage.getGenresMap();
    }

    public Map<Integer, MPA> getMpaRatingMap() {
        return mpaStorage.getMpaRatingMap();
    }

    public Map<Integer, Film> getMap() {
        Map<Integer, Film> filmsMap = new HashMap<>();
        for (Film film : getList()) {
            filmsMap.put(film.getId(), film);
        }
        return filmsMap;
    }

    public Film addInfoFromGenreAndMPAStorages(Film film) {
        film.setMpa(getMpaRatingMap().get(film.getMpa().getId()));
        for (Genre genre : film.getGenres()) {
            genre.setName(getGenresMap().get(genre.getId()).getName());
        }
        return film;
    }
}

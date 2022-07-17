package ru.yandex.practicum.filmorate.storage.mpaRatings;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Map;

public interface MPAStorage {
    public Map<Integer, MPA> getMpaRatingMap();
}

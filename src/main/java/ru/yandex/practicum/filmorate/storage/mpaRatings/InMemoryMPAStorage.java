package ru.yandex.practicum.filmorate.storage.mpaRatings;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.HashMap;
import java.util.Map;

//@Repository
public class InMemoryMPAStorage implements MPAStorage {
    private final Map<Integer, MPA> mpaRating = new HashMap<>();

    public InMemoryMPAStorage() {
        mpaRating.put(1, new MPA(1, "G"));
        mpaRating.put(2, new MPA(1, "PG"));
        mpaRating.put(3, new MPA(1, "PG-13"));
        mpaRating.put(4, new MPA(1, "R"));
        mpaRating.put(5, new MPA(1, "NC-17"));
    }

    @Override
    public Map<Integer, MPA> getMpaRatingMap() {
        return mpaRating;
    }
}

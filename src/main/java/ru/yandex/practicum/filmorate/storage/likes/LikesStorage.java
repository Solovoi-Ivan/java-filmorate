package ru.yandex.practicum.filmorate.storage.likes;

public interface LikesStorage {
    public void putLike(int filmId, int userId);

    public void removeLike(int filmId, int userId);
}

package ru.yandex.practicum.filmorate.storage.like;

import java.util.List;

public interface LikeStorage {

    void addLikeTooFilm(Long filmId, Long userId);

    void deleteLikeTooFilm(Long filmId, Long userId);

    List<Long> getFilmLikes(Long filmId);
}

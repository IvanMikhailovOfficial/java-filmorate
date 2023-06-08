package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {

    Collection<Genre> getAllGenre();

    Genre getGenre(Integer id);

    List<Genre> getGenresByFilmId(Long filmId);

    void deleteFromFilmGenre(Long filmId);

    void addGenreToFilmGenre(Film film);
}

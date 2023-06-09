package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addingAMovie(Film film);

    List<Film> getAllFilms();

    Film movieUpdate(Film film);

    Film getFilm(Long id);
}

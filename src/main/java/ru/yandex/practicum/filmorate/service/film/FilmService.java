package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film addingAMovie(Film film);

    List<Film> getAllFilms();

    Film movieUpdate(Film film);

    Film getById(Long id);

    void addLikeTooFilm(Long id, Long userId);

    void deleteLikeTooFilm(Long id, Long userId);

    List<Film> getPopularFilms(Integer count);
}

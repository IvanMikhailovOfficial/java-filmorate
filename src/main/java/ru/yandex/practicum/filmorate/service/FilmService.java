package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.errors.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FilmService {
    private Long idGenerator = 1L;

    private final Map<Long, Film> movieStorage = new HashMap<>();

    public Film addingAMovie(Film film) {
        film.setId(idGenerator);
        movieStorage.put(idGenerator, film);
        idGenerator++;
        log.debug("Фильм успешно создан " + film);
        return film;
    }

    public List<Film> getAllFilms() {
        log.debug("Фильмы успешно возвращены");
        return new ArrayList<>(movieStorage.values());
    }

    public Film movieUpdate(Film film) {
        if (movieStorage.containsKey(film.getId())) {
            log.debug("Фильм успешно обновлен " + film);
            movieStorage.put(film.getId(), film);
            return film;
        } else {
            log.debug("Невозможно обновить фильм " + film);
            throw new ValidationException("Невозможно обновить фильм с таким id");
        }
    }
}

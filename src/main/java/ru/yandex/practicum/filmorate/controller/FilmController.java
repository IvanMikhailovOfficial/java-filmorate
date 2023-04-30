package ru.yandex.practicum.filmorate.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.errors.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validateon.FilmValidator;

import java.util.List;

@RequestMapping("/films")
@RestController
public class FilmController {
    private final FilmService filmService = new FilmService();

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        try {
            FilmValidator.validate(film);
        } catch (ValidationException e) {
            return new ResponseEntity<>(film, HttpStatus.valueOf(500));
        }
        return new ResponseEntity<>(filmService.addingAMovie(film), HttpStatus.valueOf(200));
    }


    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        try {
            FilmValidator.validate(film);
        } catch (ValidationException e) {
            return new ResponseEntity<>(film, HttpStatus.valueOf(500));
        }

        try {
            return new ResponseEntity<>(filmService.movieUpdate(film), HttpStatus.valueOf(200));
        } catch (ValidationException e) {
            return new ResponseEntity<>(film, HttpStatus.valueOf(500));
        }

    }
}

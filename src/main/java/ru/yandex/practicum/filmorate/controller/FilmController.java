package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

@RequestMapping("/films")
@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        log.debug("Был вызван POST метод createFilm");
        return new ResponseEntity<>(filmService.addingAMovie(film), HttpStatus.valueOf(200));
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        log.debug("Был вызван GET метод getAllFilms");
        return new ResponseEntity<>(filmService.getAllFilms(), HttpStatus.valueOf(200));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        log.debug("Был вызван PUT метод updateFilm");
        return new ResponseEntity<>(filmService.movieUpdate(film), HttpStatus.valueOf(200));
    }

    @PutMapping("{id}/like/{userId}")
    public ResponseEntity<?> addLikeTooFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("Был вызван PUT метод addLikeTooFilm");
        filmService.addLikeTooFilm(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/like/{userId}")
    public ResponseEntity<?> deleteLikeTooFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("Был вызван DELETE метод deleteLikeTooFilm");
        filmService.deleteLikeTooFilm(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        log.debug("Был вызван GET метод getPopularFilms");
        return new ResponseEntity<>(filmService.getPopularFilms(count), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Film> getById(@PathVariable Long id) {
        log.debug("Был вызван GET метод getById");
        return new ResponseEntity<>(filmService.getById(id), HttpStatus.valueOf(200));
    }
}

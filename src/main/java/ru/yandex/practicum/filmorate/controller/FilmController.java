package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RequestMapping("/films")
@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        return new ResponseEntity<>(filmService.addingAMovie(film), HttpStatus.valueOf(200));
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        return new ResponseEntity<>(filmService.movieUpdate(film), HttpStatus.valueOf(200));
    }

    @PutMapping("{id}/like/{userId}")
    public ResponseEntity<?> addLikeTooFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeTooFilm(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/like/{userId}")
    public ResponseEntity<?> deleteLikeTooFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLikeTooFilm(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return new ResponseEntity<>(filmService.getPopularFilms(count), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Film> getById(@PathVariable Long id) {
        return new ResponseEntity<>(filmService.getById(id), HttpStatus.valueOf(200));
    }
}

package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validateon.FilmValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addingAMovie(Film film) {
        FilmValidator.validate(film);
        return filmStorage.addingAMovie(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film movieUpdate(Film film) {
        FilmValidator.validate(film);
        return filmStorage.movieUpdate(film);
    }

    @Override
    public Film getById(Long id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public void addLikeTooFilm(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        User user = userStorage.getUser(userId);
        film.getLikes().add(user.getId());
        log.debug("Лайк к фильму " + id + " успешно добавлен от юзера " + userId);
    }

    @Override
    public void deleteLikeTooFilm(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        User user = userStorage.getUser(userId);
        film.getLikes().remove(user.getId());
        log.debug("Лайк к фильму " + id + " успешно удален узером " + userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getAllFilms().stream().sorted((a, b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }
}

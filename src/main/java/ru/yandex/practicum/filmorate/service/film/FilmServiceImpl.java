package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validateon.FilmValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                           @Qualifier("userDbStorage") UserStorage userStorage,
                           LikeStorage likeStorage) {

        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
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
        likeStorage.addLikeTooFilm(film.getId(), user.getId());
    }

    @Override
    public void deleteLikeTooFilm(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        User user = userStorage.getUser(userId);
        likeStorage.deleteLikeTooFilm(film.getId(), user.getId());
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getAllFilms().stream().sorted((a, b) -> b.getLikes().size() - a.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }
}

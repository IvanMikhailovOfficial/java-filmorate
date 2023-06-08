package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
})
class FilmorateApplicationTests {

    private final FilmDbStorage filmDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final LikeDbStorage likeDbStorage;
    private final MpaDbStorage mpaDbStorage;

    private final UserDbStorage userDbStorage;

    @Test
    public void getMpaById() {
        Mpa actual = mpaDbStorage.getMpaById(2);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("PG", actual.getName());
    }

    @Test
    public void getAllMpa() {
        List<Mpa> actual = new ArrayList<>(mpaDbStorage.getAllMpa());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(5, actual.size());
    }

    @Test
    public void getAllGenre() {
        List<Genre> actual = new ArrayList<>(genreDbStorage.getAllGenre());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(6, actual.size());
    }

    @Test
    public void getGenre() {
        Genre actual = genreDbStorage.getGenre(3);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.getId());
        Assertions.assertEquals("Мультфильм", actual.getName());
    }

    @Test
    public void getGenresByFilmId() {
        Film film = new Film(2L, "Новый год", "описание",
                LocalDate.of(2022, 12, 31), 40,
                new HashSet<>(), new HashSet<>(List.of(genreDbStorage.getGenre(1))), mpaDbStorage.getMpaById(1));
        Long id = filmDbStorage.addingAMovie(film).getId();
        Genre[] expected = film.getGenres().toArray(new Genre[0]);
        Assertions.assertArrayEquals(expected, genreDbStorage.getGenresByFilmId(id).toArray());
    }

    @Test
    public void deleteFromFilmGenre() {
        Film film = new Film(2L, "Новый год", "описание",
                LocalDate.of(2022, 12, 31), 40,
                new HashSet<>(), new HashSet<>(List.of(genreDbStorage.getGenre(1))), mpaDbStorage.getMpaById(1));
        Long id = filmDbStorage.addingAMovie(film).getId();
        genreDbStorage.deleteFromFilmGenre(id);
        Assertions.assertTrue(genreDbStorage.getGenresByFilmId(id).isEmpty());
    }

    @Test
    public void addGenreToFilmGenre() {
        Film film = new Film(2L, "Новый год", "описание",
                LocalDate.of(2022, 12, 31), 40,
                new HashSet<>(), new HashSet<>(), mpaDbStorage.getMpaById(1));
        Long id = filmDbStorage.addingAMovie(film).getId();
        film.setGenres(new HashSet<>(List.of(genreDbStorage.getGenre(1))));
        film.setId(id);
        genreDbStorage.addGenreToFilmGenre(film);
        Genre[] expected = {genreDbStorage.getGenre(1)};
        Assertions.assertArrayEquals(expected, genreDbStorage.getGenresByFilmId(id).toArray());
    }

    @Test
    public void addLikeTooFilm() {
        Film film = new Film(2L, "Новый год", "описание",
                LocalDate.of(2022, 12, 31), 40,
                new HashSet<>(), new HashSet<>(), mpaDbStorage.getMpaById(1));
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        film = filmDbStorage.addingAMovie(film);
        user = userDbStorage.addUser(user);
        likeDbStorage.addLikeTooFilm(film.getId(), user.getId());
        Film filmWithLikes = filmDbStorage.getFilm(film.getId());
        Long[] expected = {user.getId()};
        Assertions.assertArrayEquals(expected, filmWithLikes.getLikes().toArray());
    }

    @Test
    public void deleteLikeTooFilm() {
        Film film = new Film(2L, "Новый год", "описание",
                LocalDate.of(2022, 12, 31), 40,
                new HashSet<>(), new HashSet<>(), mpaDbStorage.getMpaById(1));
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        film = filmDbStorage.addingAMovie(film);
        user = userDbStorage.addUser(user);
        likeDbStorage.addLikeTooFilm(film.getId(), user.getId());
        likeDbStorage.deleteLikeTooFilm(film.getId(), user.getId());
        Film filmWithLikes = filmDbStorage.getFilm(film.getId());
        Assertions.assertTrue(filmWithLikes.getLikes().isEmpty());
    }

    @Test
    public void getFilmLikes() {
        Film film = new Film(2L, "Новый год", "описание",
                LocalDate.of(2022, 12, 31), 40,
                new HashSet<>(), new HashSet<>(), mpaDbStorage.getMpaById(1));
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        User user2 = new User(3L, "3566@ya.ru", "users", "Ivann",
                LocalDate.of(1984, 6, 11));
        film = filmDbStorage.addingAMovie(film);
        user = userDbStorage.addUser(user);
        user2 = userDbStorage.addUser(user2);
        likeDbStorage.addLikeTooFilm(film.getId(), user.getId());
        likeDbStorage.addLikeTooFilm(film.getId(), user2.getId());
        Film filmWithLikes = filmDbStorage.getFilm(film.getId());
        Long[] expected = {user.getId(), user2.getId()};
        Assertions.assertArrayEquals(expected, filmWithLikes.getLikes().toArray());
    }

    @Test
    public void addUser() {
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        user = userDbStorage.addUser(user);
        Assertions.assertEquals(1, user.getId());
    }

    @Test
    public void getAllUser() {
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        User user2 = new User(3L, "3566@ya.ru", "users", "Ivann",
                LocalDate.of(1984, 6, 11));
        user = userDbStorage.addUser(user);
        user2 = userDbStorage.addUser(user2);
        User[] expected = {user, user2};
        Assertions.assertArrayEquals(expected, userDbStorage.getAllUser().toArray());
    }

    @Test
    public void userUpdate() {
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        user = userDbStorage.addUser(user);
        User userUpdayted = new User(1L, "3566@ya.ru", "users", "Ivann",
                LocalDate.of(1984, 6, 11));
        User actual = userDbStorage.userUpdate(userUpdayted);
        Assertions.assertEquals(userUpdayted, actual);
    }

    @Test
    public void getUser() {
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        user = userDbStorage.addUser(user);
        User actual = userDbStorage.getUser(user.getId());
        Assertions.assertEquals(user, actual);
    }

    @Test
    public void addingToFriends() {
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        User user2 = new User(3L, "3566@ya.ru", "users", "Ivann",
                LocalDate.of(1984, 6, 11));
        user = userDbStorage.addUser(user);
        user2 = userDbStorage.addUser(user2);
        userDbStorage.addingToFriends(user.getId(), user2.getId());
        User[] expected = {user2};
        Assertions.assertArrayEquals(expected, userDbStorage.getFriendList(user.getId()).toArray());
    }

    @Test
    public void deleteFromFriends() {
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        User user2 = new User(3L, "3566@ya.ru", "users", "Ivann",
                LocalDate.of(1984, 6, 11));
        user = userDbStorage.addUser(user);
        user2 = userDbStorage.addUser(user2);
        userDbStorage.addingToFriends(user.getId(), user2.getId());
        userDbStorage.deleteFromFriends(user.getId(), user2.getId());
        Assertions.assertTrue(userDbStorage.getFriendList(user.getId()).isEmpty());
    }

    @Test
    public void getFriendList() {
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        User user2 = new User(3L, "3566@ya.ru", "users", "Ivann",
                LocalDate.of(1984, 6, 11));
        user = userDbStorage.addUser(user);
        user2 = userDbStorage.addUser(user2);
        userDbStorage.addingToFriends(user.getId(), user2.getId());
        User[] expected = {user2};
        userDbStorage.getFriendList(user.getId());
        Assertions.assertArrayEquals(expected, userDbStorage.getFriendList(user.getId()).toArray());
    }

    @Test
    public void getCommonFriends() {
        User user = new User(2L, "123@ya.ru", "user", "Ivan",
                LocalDate.of(1960, 3, 5));
        User user2 = new User(3L, "3566@ya.ru", "users", "Ivann",
                LocalDate.of(1984, 6, 11));
        User user3 = new User(3L, "99996@ya.ru", "users21", "Petr",
                LocalDate.of(1985, 4, 1));
        user = userDbStorage.addUser(user);
        user2 = userDbStorage.addUser(user2);
        user3 = userDbStorage.addUser(user3);
        userDbStorage.addingToFriends(user.getId(), user3.getId());
        userDbStorage.addingToFriends(user2.getId(), user3.getId());
        User[] expected = {user3};
        Assertions.assertArrayEquals(expected, userDbStorage.getCommonFriends(user.getId(), user2.getId()).toArray());
    }

    @Test
    public void addingAMovie() {
        Film film = new Film(2L, "Новый год", "описание",
                LocalDate.of(2022, 12, 31), 40,
                new HashSet<>(), new HashSet<>(), mpaDbStorage.getMpaById(1));
        film = filmDbStorage.addingAMovie(film);
        Assertions.assertEquals(1, film.getId());
    }

    @Test
    public void getAllFilms() {
        Film film = new Film(2L, "Новый год", "описание",
                LocalDate.of(2022, 12, 31), 40,
                new HashSet<>(), new HashSet<>(), mpaDbStorage.getMpaById(1));
        Film film2 = new Film(2L, "Новый год2", "описание",
                LocalDate.of(2023, 12, 31), 70,
                new HashSet<>(), new HashSet<>(), mpaDbStorage.getMpaById(1));
        film = filmDbStorage.addingAMovie(film);
        film2 = filmDbStorage.addingAMovie(film2);
        Film[] expected = {film, film2};
        Assertions.assertArrayEquals(expected, filmDbStorage.getAllFilms().toArray());
    }

    @Test
    public void movieUpdate() {
        Film film = new Film(2L, "Новый год", "описание",
                LocalDate.of(2022, 12, 31), 40,
                new HashSet<>(), new HashSet<>(), mpaDbStorage.getMpaById(1));
        Film film2 = new Film(1L, "Новый год2", "описание",
                LocalDate.of(2023, 12, 31), 70,
                new HashSet<>(), new HashSet<>(), mpaDbStorage.getMpaById(1));
        filmDbStorage.addingAMovie(film);
        Film updaytedFilm = filmDbStorage.movieUpdate(film2);
        Assertions.assertEquals(film2, updaytedFilm);
    }
}

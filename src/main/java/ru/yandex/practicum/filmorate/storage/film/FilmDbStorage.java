package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component("filmDbStorage")
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Override
    public Film addingAMovie(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Long id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();
        film.setId(id);
        film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                genre.setName(genreStorage.getGenre(genre.getId()).getName());
            }
            genreStorage.addGenreToFilmGenre(film);
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        List<Film> film = jdbcTemplate.query(sql, (rs, rowNum) -> new Film(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(likeStorage.getFilmLikes(rs.getLong("id"))),
                new HashSet<>(genreStorage.getGenresByFilmId(rs.getLong("id"))),
                mpaStorage.getMpaById(rs.getInt("mpa"))
        ));
        log.debug("Все фильиы успешно получены");
        return film;
    }

    @Override
    public Film movieUpdate(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa = ? WHERE id = ?";
        if (jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId()) != 0) {
            Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());
            film.setMpa(mpa);
            if (!film.getGenres().isEmpty()) {
                genreStorage.deleteFromFilmGenre(film.getId());
                genreStorage.addGenreToFilmGenre(film);
            } else {
                genreStorage.deleteFromFilmGenre(film.getId());
            }
            film.setGenres(new HashSet<>(genreStorage.getGenresByFilmId(film.getId())));
            return film;
        } else {
            log.debug("Фильма с таким id {} не существует", film.getId());
            throw new NotFoundException("Фильма с таким " + film.getId() + " не существует");
        }
    }

    @Override
    public Film getFilm(Long id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        Film film;
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.first()) {
            film = new Film(
                    sqlRowSet.getLong("id"),
                    sqlRowSet.getString("name"),
                    sqlRowSet.getString("description"),
                    Objects.requireNonNull(sqlRowSet.getDate("release_date")).toLocalDate(),
                    sqlRowSet.getInt("duration"),
                    new HashSet<>(likeStorage.getFilmLikes(sqlRowSet.getLong("id"))),
                    new HashSet<>(genreStorage.getGenresByFilmId(sqlRowSet.getLong("id"))),
                    mpaStorage.getMpaById(sqlRowSet.getInt("mpa"))
            );
        } else {
            log.debug("Фильм с id {} не может быть предоставлен", id);
            throw new NotFoundException("Фильм с " + id + " не может быть предоставлен");
        }
        log.debug("Фильм с id {} получен", id);
        return film;
    }

    @Override
    public Film deleteFilmById(Long id) {
        String sql = "DELETE FROM films WHERE id = ?";
        Film film = getFilm(id);
        if (jdbcTemplate.update(sql, id) != 0) {
            log.debug("Удалениее фильма с id {} прошло успешно", id);
            return film;
        } else {
            log.debug("Удаление фильма с id {} не удалось", id);
            throw new NotFoundException("Удаление фильма с " + id + " не удалось");
        }
    }
}

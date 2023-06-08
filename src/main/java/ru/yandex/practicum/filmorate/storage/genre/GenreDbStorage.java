package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("genreDbStorage")
@Slf4j
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenre() {
        String sql = "SELECT * FROM GENRES";
        List<Genre> listGenre = jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("name")));
        log.debug("Все жанры успешно получены");
        return listGenre.stream().sorted(Comparator.comparingInt(Genre::getId)).collect(Collectors.toList());
    }

    @Override
    public Genre getGenre(Integer id) {
        String sql = "SELECT * FROM GENRES WHERE genre_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        Genre genre = null;
        if (sqlRowSet.first()) {
            genre = new Genre(sqlRowSet.getInt("genre_id"), sqlRowSet.getString("name"));
        } else {
            log.debug("Жанр с id " + id + " не существует");
            throw new NotFoundException("Жанр с id " + id + " не существует");
        }
        log.debug("Жанр с id {} успешно получен", id);
        return genre;
    }

    @Override
    public List<Genre> getGenresByFilmId(Long filmId) {
        String sql = "SELECT FIG.genre_id, GEN.name FROM film_genre AS FIG " +
                "INNER JOIN GENRES AS GEN ON FIG.genre_id = GEN.genre_id WHERE FIG.film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("name")), filmId);
    }

    @Override
    public void deleteFromFilmGenre(Long filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void addGenreToFilmGenre(Film film) {
        for (Genre genre : film.getGenres()) {
            String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }
}

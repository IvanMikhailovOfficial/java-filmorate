package ru.yandex.practicum.filmorate.validateon;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.errors.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {
    public static void validate(Film film) {
        if (film.getName().isBlank()) {
            log.debug("Имя фильма не может быть пустым " + film);
            throw new ValidationException("Имя фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.debug("Длина описания не может превышать 200 симвлов " + film);
            throw new ValidationException("Длина описания не может превышать 200 симвлов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Дата создания фильма не может быть до 28 декабря 1895 года " + film);
            throw new ValidationException("Дата создания фильма не может быть до 28 декабря 1895 года");
        }

        if (film.getDuration() < 0) {
            log.debug("Количество минут не может быть меньше 0 " + film);
            throw new ValidationException("Количество минут не может быть меньше 0");
        }
    }
}

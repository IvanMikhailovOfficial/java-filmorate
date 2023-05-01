package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.errors.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validateon.FilmValidator;

import java.time.LocalDate;

public class FilmValidatorTest {

    private Film film;

    @BeforeEach
    public void init() {
        film = new Film(1L, "Java", " 1_000_000",
                LocalDate.of(2000, 12, 25), 96);
    }


    @Test
    public void validateShoudentThrowExceptionWhenFilmValid() {
        Assertions.assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    public void validateShouldThrowExceptionWhenNameIsBlank() {
        film.setName("");
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        Assertions.assertEquals("Имя фильма не может быть пустым", validationException.getMessage());
    }

    @Test
    public void validateShouldTrowExceptionWhenDescriptionSizeMoreThen200() {
        film.setDescription("hello".repeat(100));
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        Assertions.assertEquals("Длина описания не может превышать 200 симвлов",
                validationException.getMessage());
    }

    @Test
    public void validateShouldThrowExceptionWhenReleaseDateWrong() {
        film.setReleaseDate(LocalDate.of(1800, 12, 25));
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        Assertions.assertEquals("Дата создания фильма не может быть до 28 декабря 1895 года",
                validationException.getMessage());
    }

    @Test
    public void validateShouldThrowExceptionWnenDurationIsNegative() {
        film.setDuration(-1);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> FilmValidator.validate(film));
        Assertions.assertEquals("Количество минут не может быть меньше 0", validationException.getMessage());
    }
}

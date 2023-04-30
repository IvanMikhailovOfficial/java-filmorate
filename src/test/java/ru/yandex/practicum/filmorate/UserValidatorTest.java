package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.errors.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validateon.UserValidator;

import java.time.LocalDate;

public class UserValidatorTest {

    @Test
    public void validateShoudentThrowExceptionWhenUserValid() {

        User user = new User(1L, "tyyu557@gmail.com", "12345", "tyyu",
                LocalDate.of(2000, 12, 23));
        Assertions.assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    public void validateShouldTrowExceptionWhenNotCorrectEmail() {

        User user = new User(1L, "", "12345", "tyyu",
                LocalDate.of(2000, 12, 23));
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validate(user));
        Assertions.assertEquals("Некорректный email", validationException.getMessage());
    }

    @Test
    public void validateSholdTrowExceptionWhenLoginIsWrong() {

        User user = new User(1L, "tyyu557@gmail.com", "", "tyyu",
                LocalDate.of(2000, 12, 25));
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validate(user));
        Assertions.assertEquals("Неверный логин", validationException.getMessage());
    }

    @Test
    public void validateShouldSwitchNameWhenWhenItWrong() {

        User user = new User(1L, "tyyu557@gmail.com", "12345", "",
                LocalDate.of(2000, 12, 25));
        UserValidator.validate(user);
        Assertions.assertEquals("12345", user.getName());
    }

    @Test
    public void validateShouldTrowExceptionWhenDateInFuture() {

        User user = new User(1L, "tyyu557@gmail.com", "12345", "tyyu",
                LocalDate.of(2900, 12, 25));
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> UserValidator.validate(user));
        Assertions.assertEquals("Дата рождения не будет в будущем", validationException.getMessage());
    }
}

package ru.yandex.practicum.filmorate.validateon;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.errors.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public static void validate(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("Некорректный email у " + user);
            throw new ValidationException("Некорректный email");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Неверный логин " + user);
            throw new ValidationException("Неверный логин");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения не будет в будущем " + user);
            throw new ValidationException("Дата рождения не будет в будущем");
        }
    }
}

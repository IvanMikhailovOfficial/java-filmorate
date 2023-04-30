package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.errors.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserService {
    private Long idGenerator = 1L;

    private final Map<Long, User> userStorage = new HashMap<>();

    public User addUser(User user) {
        user.setId(idGenerator);
        userStorage.put(idGenerator, user);
        idGenerator++;
        log.debug("Пользователь добавлен " + user);
        return user;
    }

    public List<User> getAllUser() {
        log.debug("Получение всех пользователей ");
        return new ArrayList<>(userStorage.values());
    }

    public User userUpdate(User user) {
        if (userStorage.containsKey(user.getId())) {
            log.debug("Пользователь изменен " + user);
            userStorage.put(user.getId(), user);
            return user;
        } else {
            log.debug("Ошибка обновления пользователя " + user);
            throw new ValidationException("Ошибка обновления пользователя");
        }
    }
}

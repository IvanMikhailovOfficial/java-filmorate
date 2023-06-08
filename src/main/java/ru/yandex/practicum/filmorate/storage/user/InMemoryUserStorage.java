package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

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
            throw new NotFoundException("Ошибка обновления пользователя");
        }
    }

    @Override
    public User getUser(Long id) {
        if (userStorage.containsKey(id)) {
            return userStorage.get(id);
        } else {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
    }

    @Override
    public void addingToFriends(Long id, Long friendId) {
    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
    }

    @Override
    public List<User> getFriendList(Long id) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return null;
    }
}

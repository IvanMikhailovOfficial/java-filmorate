package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validateon.UserValidator;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addUser(User user) {
        UserValidator.validate(user);
        return userStorage.addUser(user);
    }

    @Override
    public List<User> getAllUser() {
        return userStorage.getAllUser();
    }

    @Override
    public User userUpdate(User user) {
        UserValidator.validate(user);
        return userStorage.userUpdate(user);
    }

    @Override
    public void addingToFriends(Long id, Long friendId) {
        userStorage.addingToFriends(id, friendId);
    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
        userStorage.deleteFromFriends(id, friendId);
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.getUser(id);
    }

    @Override
    public List<User> getFriendList(Long id) {
        return userStorage.getFriendList(id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}
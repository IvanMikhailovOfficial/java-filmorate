package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validateon.UserValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
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
        User friend1 = userStorage.getUser(id);
        User friend2 = userStorage.getUser(friendId);
        friend1.getFriends().add(friendId);
        friend2.getFriends().add(id);
    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
        User friend1 = userStorage.getUser(id);
        User friend2 = userStorage.getUser(friendId);
        friend1.getFriends().remove(friendId);
        friend2.getFriends().remove(id);
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.getUser(id);
    }

    @Override
    public List<User> getFriendList(Long id) {
        User user = userStorage.getUser(id);
        List<User> listUser = new ArrayList<>();
        for (Long element : user.getFriends()) {
            listUser.add(userStorage.getUser(element));
        }
        return listUser;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return userStorage.getUser(id).getFriends().stream()
                .filter(userId -> userStorage.getUser(otherId).getFriends().contains(userId))
                .map(userId -> userStorage.getUser(userId)).collect(Collectors.toList());
    }
}
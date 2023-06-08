package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    List<User> getAllUser();

    User userUpdate(User user);

    void addingToFriends(Long id, Long friendId);

    void deleteFromFriends(Long id, Long friendId);

    User getUserById(Long id);

    List<User> getFriendList(Long id);

    List<User> getCommonFriends(Long id, Long otherId);
}

package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    List<User> getAllUser();

    User userUpdate(User user);

    User getUser(Long id);

    void addingToFriends(Long id, Long friendId);

    void deleteFromFriends(Long id, Long friendId);

    List<User> getFriendList(Long id);

    List<User> getCommonFriends(Long id, Long otherId);
}




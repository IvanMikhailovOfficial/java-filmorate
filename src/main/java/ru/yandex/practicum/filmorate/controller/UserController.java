package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.valueOf(201));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.valueOf(200));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return new ResponseEntity<User>(userService.userUpdate(user), HttpStatus.valueOf(200));
    }

    @PutMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> addingToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addingToFriends(id, friendId);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @DeleteMapping("{id}/friends/{friendsId}")
    public ResponseEntity<?> deleteFromFriends(@PathVariable Long id, @PathVariable Long friendsId) {
        userService.deleteFromFriends(id, friendsId);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @GetMapping("{id}/friends")
    public ResponseEntity<List<User>> getFriendList(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getFriendList(id), HttpStatus.OK);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return new ResponseEntity<>(userService.getCommonFriends(id, otherId), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.valueOf(200));
    }
}

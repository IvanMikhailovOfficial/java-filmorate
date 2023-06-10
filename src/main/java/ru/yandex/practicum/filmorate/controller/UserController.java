package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/users")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid User user) {
        log.debug("Был вызван POST метод create");
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.valueOf(201));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.debug("Был вызван GET метод getAllUsers");
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.valueOf(200));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user) {
        log.debug("Был вызван PUT метод updateUser");
        return new ResponseEntity<User>(userService.userUpdate(user), HttpStatus.valueOf(200));
    }

    @PutMapping("{id}/friends/{friendId}")
    public ResponseEntity<?> addingToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("Был вызван PUT метод addingToFriends");
        userService.addingToFriends(id, friendId);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @DeleteMapping("{id}/friends/{friendsId}")
    public ResponseEntity<?> deleteFromFriends(@PathVariable Long id, @PathVariable Long friendsId) {
        log.debug("Был вызван DELETE метод deleteFromFriends");
        userService.deleteFromFriends(id, friendsId);
        return new ResponseEntity<>(HttpStatus.valueOf(200));
    }

    @GetMapping("{id}/friends")
    public ResponseEntity<List<User>> getFriendList(@PathVariable Long id) {
        log.debug("Был вызван GET метод getFriendList");
        return new ResponseEntity<>(userService.getFriendList(id), HttpStatus.OK);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.debug("Был вызван GET метод getCommonFriends");
        return new ResponseEntity<>(userService.getCommonFriends(id, otherId), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.debug("Был вызван GET метод getUserById");
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.valueOf(200));
    }
}

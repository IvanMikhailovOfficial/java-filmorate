package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.errors.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validateon.UserValidator;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService = new UserService();

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        try {
            UserValidator.validate(user);
        } catch (ValidationException e) {
            return new ResponseEntity<>(user, HttpStatus.valueOf(500));
        }
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.valueOf(201));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.valueOf(200));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        try {
            UserValidator.validate(user);
        } catch (ValidationException e) {
            return new ResponseEntity<>(user, HttpStatus.valueOf(500));
        }
        try {
            return new ResponseEntity<User>(userService.userUpdate(user), HttpStatus.valueOf(200));
        } catch (ValidationException e) {
            return new ResponseEntity<>(userService.userUpdate(user), HttpStatus.NOT_FOUND);
        }

    }
}

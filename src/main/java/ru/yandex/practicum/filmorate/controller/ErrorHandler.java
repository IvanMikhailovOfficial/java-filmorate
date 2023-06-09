package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.errors.ValidationException;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundHandler(final NotFoundException e) {
        return new ResponseEntity<>(Map.of(e.getMessage(), "NotFoundException"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validateHandler(final ValidationException e) {
        return new ResponseEntity<>(Map.of(e.getMessage(), "ValidationException"), HttpStatus.valueOf(400));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusHandler(final ResponseStatusException e) {
        return new ResponseEntity<>(Map.of(e.getMessage(), "ResponseStatusException"), e.getStatus());
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public ResponseEntity<Collection<Mpa>> getAllMpa() {
        log.debug("Был вызван GET метод getAllMpa");
        return new ResponseEntity<>(mpaService.getAllMpa(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getMpaById(@PathVariable Integer id) {
        log.debug("Был вызван GET метод getMpaById с id {}", id);
        return new ResponseEntity<>(mpaService.getMpaById(id), HttpStatus.OK);
    }
}

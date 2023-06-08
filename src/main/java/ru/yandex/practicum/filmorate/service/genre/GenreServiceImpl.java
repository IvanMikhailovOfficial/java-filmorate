package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreServiceImpl(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public Collection<Genre> getAllGenre() {
        return genreStorage.getAllGenre();
    }

    @Override
    public Genre getGenre(Integer id) {
        return genreStorage.getGenre(id);
    }
}

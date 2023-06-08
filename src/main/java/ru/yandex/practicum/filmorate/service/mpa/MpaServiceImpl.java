package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MpaServiceImpl implements MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaServiceImpl(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa().stream()
                .sorted(Comparator.comparingInt(Mpa::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Mpa getMpaById(Integer id) {
        return mpaStorage.getMpaById(id);
    }
}

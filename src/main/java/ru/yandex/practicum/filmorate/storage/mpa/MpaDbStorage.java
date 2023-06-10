package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;

@Component("mpaDbStorage")
@Slf4j
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Collection<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        List<Mpa> listMpa = jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(rs.getInt("id"),
                rs.getString("name")));
        log.debug("Все рейтинги успешно получены");
        return listMpa;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sql = "SELECT * FROM mpa WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        Mpa mpa = null;
        if (sqlRowSet.first()) {
            mpa = new Mpa(sqlRowSet.getInt("id"), sqlRowSet.getString("name"));
        } else {
            log.debug("Пользователь с таким id не найден");
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        log.debug("Mpa с id {} получен", id);
        return mpa;
    }
}

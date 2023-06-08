package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.errors.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Objects;

@Component("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Long id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        user.setId(id);
        log.debug("Юзер получен");
        return user;
    }

    @Override
    public List<User> getAllUser() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()
        ));
        log.debug("Получаем всех юзеров");
        return users;
    }

    @Override
    public User userUpdate(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        existUser(user.getId());
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.debug("Юзер с id {} был обновлен", user.getId());
        return user;
    }

    @Override
    public User getUser(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.first()) {
            User user = new User(
                    sqlRowSet.getLong("id"),
                    sqlRowSet.getString("email"),
                    sqlRowSet.getString("login"),
                    sqlRowSet.getString("name"),
                    Objects.requireNonNull(sqlRowSet.getDate("birthday")).toLocalDate()
            );
            log.debug("Пользователь с id {} получен", id);
            return user;
        } else {
            log.debug("Пользователь с id {} не получен", id);
            throw new NotFoundException("Пользователь с " + id + " не получен");
        }
    }

    @Override
    public void addingToFriends(Long id, Long friendId) {
        existUser(id);
        existUser(friendId);
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, id, friendId);
        } catch (DuplicateKeyException e) {
            log.debug("Невозможно добавить в друзья, если уже есть в списке друзей");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Невозможно добавить в друзья, " +
                    "если уже есть в списке друзей");
        } catch (DataIntegrityViolationException e) {
            log.debug("Невозможно добавить в друзья, самого себя");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Невозможно добавить в друзья, " +
                    "самого себя");
        }
    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
        existUser(id);
        existUser(friendId);
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        if (jdbcTemplate.update(sql, id, friendId) == 0) {
            log.debug("Удаление из друзей прошло не успешно, так как пользователи не в друзьях");
            throw new NotFoundException("Удаление из друзей прошло не успешно, так как пользователи не в друзьях");
        } else {
            log.debug("Удаление из друзей с id {} из списка friendId {} прошло не успешно", id, friendId);
        }
    }

    public void existUser(Long userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getDate("birthday").toLocalDate()
            ), userId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Юзер с id {} не найден", userId);
            throw new NotFoundException("Юзер с " + userId + " не найден");
        }
    }

    @Override
    public List<User> getFriendList(Long id) {
        String sql = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        existUser(id);
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()
        ), id);
        log.debug("Список друзей юзера с id {} получен", id);
        return users;
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        existUser(id);
        existUser(otherId);
        String sql = "SELECT * FROM users WHERE id IN " +
                "(SELECT friend_id FROM friends WHERE user_id = ? AND friend_id IN " +
                "(SELECT friend_id FROM friends WHERE user_id = ?))";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()
        ), id, otherId);
        log.debug("Получение общих друзей между пользователем id {} и пользователем id {} успешно произошло",
                id, otherId);
        return users;
    }
}
package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private final Set<User> friends = new HashSet<>();

    public Map<String, Object> toMap() {
        System.out.println(this);
        return Map.of(
                "email", email,
                "login", login,
                "name", name,
                "birthday", birthday
        );
    }
}

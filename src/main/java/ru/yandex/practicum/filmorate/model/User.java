package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @Email(message = "Не корректный email")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    private String login;
    private String name;
    @Past(message = "День рождения не может быть в будущем")
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

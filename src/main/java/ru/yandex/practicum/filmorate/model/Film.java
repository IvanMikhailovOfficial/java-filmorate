package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Long id;
    @NotBlank(message = "Имя фильмане может быть пустым")
    private String name;
    @Size(max = 200, message = "Длинна не может превышать 200 символов")
    private String description;
    @PastOrPresent(message = "Неверная дата релиза")
    private LocalDate releaseDate;
    @Positive(message = "Не может быть отрицательная длительность")
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
    @NotNull
    private Mpa mpa;

    public Map<String, Object> toMap() {
        return Map.of(
                "name", name,
                "description", description,
                "release_date", releaseDate,
                "duration", duration,
                "mpa", mpa.getId()
        );
    }
}

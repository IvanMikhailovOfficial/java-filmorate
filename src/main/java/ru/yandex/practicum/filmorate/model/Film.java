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
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
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

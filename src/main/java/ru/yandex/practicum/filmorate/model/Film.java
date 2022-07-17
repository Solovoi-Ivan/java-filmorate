package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
public class Film {
    private Set<Integer> likesSet = new HashSet<>();
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private int id;
    @NotNull
    private String name;
    @NotNull
    @Size(min = 2, max = 200, message = "Длина текста превышает 200 символов")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private int duration;
    @NotNull
    private int rate;
    @NotNull
    private MPA mpa;
}
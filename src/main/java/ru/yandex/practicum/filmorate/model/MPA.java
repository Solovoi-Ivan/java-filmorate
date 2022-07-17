package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MPA {
    private int id;
    private String name;

    public MPA(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
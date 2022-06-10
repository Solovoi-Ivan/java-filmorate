package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data // Добавил аннотации
public class User {
    private int id;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "[^\\s]+")
    private String login;
    @NotNull
    private String name;
    @NotNull
    private LocalDate birthday;
}
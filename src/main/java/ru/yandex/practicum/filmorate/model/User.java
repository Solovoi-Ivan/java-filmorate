package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.*;

@Data
public class User {
    private Set<Integer> friendsSet = new HashSet<>();
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
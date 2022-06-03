package ru.yandex.practicum.filmorate.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.FilmorateApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    FilmController fc = new FilmController();

//    @BeforeAll
//    static void serverTestEnvironment() {
//        SpringApplication.run(FilmorateApplication.class);
//    }

//    @Test
//    void PostFilmEndpointTest() throws IOException, InterruptedException {
//        LocalDate date = LocalDate.of(2022, MAY, 10);
//        Duration time = Duration.ofHours(2);
//        Film film = new Film(1, "a", "b", date, time);
//        Gson gson = new GsonBuilder().serializeNulls().create();
//        String json = gson.toJson(film);
//        HttpClient client = HttpClient.newHttpClient();
//        URI uri = URI.create("http://localhost:8080/film");
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(uri)
//                .POST(HttpRequest.BodyPublishers.ofString(json))
//                .build();
//        client.send(request, HttpResponse.BodyHandlers.ofString());
//
//
//        URI uri2 = URI.create("http://localhost:8080/films");
//        HttpRequest request2 = HttpRequest.newBuilder()
//                .uri(uri2)
//                .header("Accept", "application/json")
//                .GET()
//                .build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//
//
//        assertEquals(gson.fromJson(response.body(), Film.class), film);
//    }
}

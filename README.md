# java-filmorate
Template repository for Filmorate project.
![Image](Schema.png)

Структура БД:
films - информация о фильме
mpa_ratings - таблица, связывающая id рейтинга с его описанием
film_genres - таблица, связывающая id фильма с id его жанров
genres - таблица, связывающая id жанра с его описанием
users - информация о пользователе
friendships - таблица, отображающая добавленных друзей пользователя
likes - информация о лайках, поставленным фильму пользователями

Примеры:
SELECT *
FROM films; // отобразить всю информацию о всех фильмах

SELECT name
FROM films
JOIN film_genres ON films.film_id = film_genres.film_id
JOIN genres ON film_genres.genre_id = genres.genre_id
WHERE genres.genre_type = 'Боевик'; // отобразить названия фильмов с жанром "Боевик"


Код схемы:

Table films {
film_id int [pk, increment]
film_name varchar(200)
description varchar(200)
release_date date
duration int
mpa_rating_id int
}

Table mpa_ratings {
mpa_rating_id int [pk, ref: < films.mpa_rating_id]
mpa_rating_type varchar(5)
}

Table film_genres {
film_id int [ref: > films.film_id]
genre_id int
}

Table genres {
genre_id int [pk, increment, ref: < film_genres.genre_id]
genre_type varchar(200)
}

Table users {
user_id int [pk, increment]
email varchar(200)
login varchar(200)
user_name varchar(200)
birthday date
}

Table friendships {
first_user_id int [ref: > users.user_id]
second_user_id int [ref: > users.user_id]
}

Table likes {
film_id int [ref: > films.film_id]
user_id int [ref: > users.user_id]
}
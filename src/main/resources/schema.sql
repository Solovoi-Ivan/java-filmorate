CREATE TABLE IF NOT EXISTS GENRES
(
    genre_id   INT PRIMARY KEY AUTO_INCREMENT,
    genre_type VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS MPA_RATINGS
(
    mpa_rating_id          INT PRIMARY KEY AUTO_INCREMENT,
    mpa_rating_description VARCHAR(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILMS
(
    film_id       INT PRIMARY KEY AUTO_INCREMENT,
    film_name     VARCHAR(200) NOT NULL,
    description   VARCHAR(200) NOT NULL,
    release_date  DATE         NOT NULL,
    duration      INT          NOT NULL,
    mpa_rating_id INT          NOT NULL REFERENCES MPA_RATINGS (mpa_rating_id)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES
(
    film_id  INT REFERENCES FILMS (film_id) ON DELETE CASCADE,
    genre_id INT REFERENCES GENRES (genre_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS USERS
(
    user_id   INT PRIMARY KEY AUTO_INCREMENT,
    email     VARCHAR(200) NOT NULL,
    login     VARCHAR(200) NOT NULL,
    user_name VARCHAR(200) NOT NULL,
    birthday  DATE         NOT NULL
);

CREATE TABLE IF NOT EXISTS FRIENDSHIPS
(
    first_user_id     INT REFERENCES USERS (user_id) ON DELETE CASCADE,
    second_user_id    INT REFERENCES USERS (user_id) ON DELETE CASCADE,
    PRIMARY KEY (first_user_id, second_user_id)
);

CREATE TABLE IF NOT EXISTS USER_FILM_LIKES
(
    film_id INT REFERENCES FILMS (film_id) ON DELETE CASCADE,
    user_id INT REFERENCES USERS (user_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
)
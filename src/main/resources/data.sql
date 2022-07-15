MERGE INTO GENRES (genre_id, genre_type)
    VALUES (1, 'Комедия');

MERGE INTO GENRES (genre_id, genre_type)
    VALUES (2, 'Драма');

MERGE INTO GENRES (genre_id, genre_type)
    VALUES (3, 'Мультфильм');

MERGE INTO GENRES (genre_id, genre_type)
    VALUES (4, 'Триллер');

MERGE INTO GENRES (genre_id, genre_type)
    VALUES (5, 'Документальный');

MERGE INTO GENRES (genre_id, genre_type)
    VALUES (6, 'Боевик');

MERGE INTO MPA_RATINGS (mpa_rating_id, mpa_rating_description)
    VALUES (1, 'G');

MERGE INTO MPA_RATINGS (mpa_rating_id, mpa_rating_description)
    VALUES (2, 'PG');

MERGE INTO MPA_RATINGS (mpa_rating_id, mpa_rating_description)
    VALUES (3, 'PG-13');

MERGE INTO MPA_RATINGS (mpa_rating_id, mpa_rating_description)
    VALUES (4, 'R');

MERGE INTO MPA_RATINGS (mpa_rating_id, mpa_rating_description)
    VALUES (5, 'NC-17');
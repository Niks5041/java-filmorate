
CREATE TABLE IF NOT EXISTS "user" (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    birthday TIMESTAMP,
    email VARCHAR(255),
    login VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS genres (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS mpa (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS film (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(1000),
    releaseDate TIMESTAMP,
    duration INTEGER,
    mpa_id INTEGER REFERENCES mpa(id)
);

CREATE TABLE IF NOT EXISTS friends (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER REFERENCES "user"(id),
    friend_id INTEGER,
    friendship_status BOOLEAN
);

CREATE TABLE IF NOT EXISTS likes (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER  REFERENCES "user"(id),
    film_id INTEGER REFERENCES film(id)
);

CREATE TABLE IF NOT EXISTS film_genre (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER REFERENCES film(id),
    genre_id INTEGER  REFERENCES genres(id)
);







CREATE TABLE trips (
       id integer PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
       author_id integer REFERENCES users(id),
       name VARCHAR NOT NULL,
       country_code CHAR(2)
);
--;;
CREATE INDEX trips_author_id ON trips(author_id);

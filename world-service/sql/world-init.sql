DROP SCHEMA IF EXISTS world CASCADE;

CREATE SCHEMA world;

CREATE TABLE world.city
(
    ID            VARCHAR PRIMARY KEY,
    NAME          VARCHAR NOT NULL,
    FACTORY_SLOTS INT     NOT NULL DEFAULT 0,
    LATITUDE      FLOAT   NOT NULL,
    LONGITUDE     FLOAT   NOT NULL
);

INSERT INTO world.city(ID, NAME, FACTORY_SLOTS, LATITUDE, LONGITUDE)
VALUES ('Warsaw', 'Warsaw', 5, 52.2297, 21.0122),
       ('Wroclaw', 'Wroclaw', 4, 51.1079, 17.0385),
       ('Opole', 'Opole', 1, 50.6751, 17.9213),
       ('Lodz', 'Łódź', 6, 51.771, 19.474),
       ('Poznan', 'Poznań', 4, 52.407, 16.93),
       ('Szczecin', 'Szczecin', 2, 53.429, 14.553),
       ('Katowice', 'Katowice', 3, 50.258, 19.028),
       ('Gdynia', 'Gdynia', 5, 54.519, 18.532),
       ('Berlin', 'Berlin', 7, 52.524, 13.411),
       ('Hamburg', 'Hamburg', 3, 53.575, 10.015),
       ('Nuremberg', 'Nuremberg', 2, 49.4609, 11.0618),
       ('Paris', 'Paris', 4, 48.853, 2.349),
       ('Nice', 'Nice', 3, 43.703, 7.266);


GRANT ALL PRIVILEGES ON SCHEMA world to "world-user";
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA world TO "world-user";
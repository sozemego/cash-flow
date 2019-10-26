DROP SCHEMA IF EXISTS game_event CASCADE;

CREATE SCHEMA game_event;

CREATE TABLE game_event.game_event (
    ID UUID PRIMARY KEY,
    TEXT VARCHAR NOT NULL,
    TIMESTAMP BIGINT NOT NULL,
    LEVEL VARCHAR NOT NULL
);

CREATE INDEX event_timestamp ON game_event.game_event (TIMESTAMP);

GRANT ALL PRIVILEGES ON SCHEMA game_event to "game-event-user";
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA game_event TO "game-event-user";
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA game_event TO "game-event-user";
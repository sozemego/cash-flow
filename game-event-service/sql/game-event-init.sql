DROP SCHEMA IF EXISTS game_event CASCADE;

CREATE SCHEMA game_event;

GRANT ALL PRIVILEGES ON SCHEMA game_event to "game-event-user";
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA game_event TO "game-event-user";
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA game_event TO "game-event-user";
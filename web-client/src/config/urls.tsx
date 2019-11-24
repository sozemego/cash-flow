export const API_GATEWAY_URL = "http://localhost:8999";
export const WORLD_SERVICE_URL = `${API_GATEWAY_URL}/world-service`;
export const WORLD_SERVICE_RESOURCES_URL = `${WORLD_SERVICE_URL}/world/resources`;
export const WORLD_SERVICE_CITIES_URL = `${WORLD_SERVICE_URL}/world`;

export const CLOCK_SERVICE_URL = `${API_GATEWAY_URL}/clock-service`;
export const CLOCK_SERVICE_CLOCK_URL = `${CLOCK_SERVICE_URL}/clock`;

export const FACTORY_SERVICE_URL = `${API_GATEWAY_URL}/factory-service`;
export const FACTORY_SERVICE_URL_EVENTS = `${FACTORY_SERVICE_URL}/events`;
export const FACTORY_SERVICE_SOCKET_URL = "ws://localhost:9001/websocket";

export const TRUCK_SERVICE_SOCKET_URL = "ws://localhost:9003/websocket";

export const PLAYER_SERVICE_URL = `${API_GATEWAY_URL}/player-service`;
export const PLAYER_SERVICE_PLAYER_URL = `${PLAYER_SERVICE_URL}/player`;
export const PLAYER_SERVICE_PLAYER_BY_USER_ID_URL = `${PLAYER_SERVICE_URL}/playerByUser`;
export const PLAYER_SERVICE_PLAYERS_URL = `${PLAYER_SERVICE_URL}/players`;

export const PLAYER_SERVICE_SOCKET_URL = "ws://localhost:9005/websocket";

export const GAME_EVENT_SERVICE_SOCKET_URL = "ws://localhost:9006/websocket";

export const AUTH_SERVICE_URL = `${API_GATEWAY_URL}/auth-service`;
export const AUTH_SERVICE_SIGN_UP_URL = `${AUTH_SERVICE_URL}/auth/create`;
export const AUTH_SERVICE_LOGIN_URL = `${AUTH_SERVICE_URL}/auth/login`;

export const API_GATEWAY_URL = "http://localhost:8999";
export const WORLD_SERVICE_URL = `${API_GATEWAY_URL}/world-service`;
export const WORLD_SERVICE_RESOURCES_URL = `${WORLD_SERVICE_URL}/world/resources`;
export const WORLD_SERVICE_CITIES_URL = `${WORLD_SERVICE_URL}/world`;

export const FACTORY_SERVICE_URL = `${API_GATEWAY_URL}/factory-service`;
export const FACTORY_SERVICE_URL_EVENTS = `${FACTORY_SERVICE_URL}/events`;
export const FACTORY_SERVICE_SOCKET_URL = "ws://localhost:9001/websocket";

export const TRUCK_SERVICE_SOCKET_URL = "ws://localhost:9003/websocket";

export const PLAYER_SERVICE_URL = `${API_GATEWAY_URL}/player-service`;
export const PLAYER_SERVICE_PLAYER_URL = `${PLAYER_SERVICE_URL}/player`;
export const PLAYER_SERVICE_SOCKET_URL = "ws://localhost:9005/websocket";

export const GAME_EVENT_SERVICE_SOCKET_URL = "ws://localhost:9006/websocket";

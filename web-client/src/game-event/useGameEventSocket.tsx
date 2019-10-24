import { GAME_EVENT_SERVICE_SOCKET_URL } from "../config/urls";
import { useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";

export function useGameEventSocket() {
    const dispatch = useDispatch();

    const { socket, readyState } = useWebsocket(
        GAME_EVENT_SERVICE_SOCKET_URL,
        dispatch
    );

    return { socket, readyState };
}

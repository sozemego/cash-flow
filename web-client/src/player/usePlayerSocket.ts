import { PLAYER_SERVICE_SOCKET_URL } from "../config/urls";
import { useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";

export function usePlayerSocket() {
  const dispatch = useDispatch();

  const { socket, readyState } = useWebsocket(
    PLAYER_SERVICE_SOCKET_URL,
    dispatch
  );

  return { socket, readyState };
}

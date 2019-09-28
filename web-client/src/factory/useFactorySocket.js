import { FACTORY_SERVICE_SOCKET_URL } from "../config/urls";
import { useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";

/**
 *
 * @returns {{readyState: *, socket: WebSocket}}
 */
export function useFactorySocket() {
  const dispatch = useDispatch();

  const { socket, readyState } = useWebsocket(
    FACTORY_SERVICE_SOCKET_URL,
    dispatch
  );

  return { socket, readyState };
}

import { TRUCK_SERVICE_SOCKET_URL } from "../config/urls";
import { useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";

export function useTruckSocket() {
  const dispatch = useDispatch();

  const { socket, readyState } = useWebsocket(
    TRUCK_SERVICE_SOCKET_URL,
    dispatch
  );

  return { socket, readyState };
}

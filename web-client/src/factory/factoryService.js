import { FACTORY_SERVICE_URL } from "../config/urls";
import { useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";

export function useFactorySocket() {
  const dispatch = useDispatch();

  const { socket, readyState } = useWebsocket(
    FACTORY_SERVICE_URL + "/websocket",
    dispatch
  );

  return { socket, readyState };
}

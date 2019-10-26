import { FACTORY_SERVICE_SOCKET_URL } from "../config/urls";
import { UseWebSocket, useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";
import { factoryInit } from "./actions";
import { useCallback } from "react";

export function useFactorySocket(): UseWebSocket {
  const dispatch = useDispatch();

  const reset = useCallback(() => dispatch(factoryInit()), [dispatch]);

  const { socket, readyState } = useWebsocket(
    FACTORY_SERVICE_SOCKET_URL,
    dispatch,
    reset
  );

  return { socket, readyState };
}

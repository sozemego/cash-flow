import { PLAYER_SERVICE_SOCKET_URL } from "../config/urls";
import { useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";
import { playerInit } from "./actions";
import { useCallback } from "react";

export function usePlayerSocket() {
  const dispatch = useDispatch();

  const reset = useCallback(() => dispatch(playerInit()), [dispatch]);

  const { socket, readyState } = useWebsocket(
    PLAYER_SERVICE_SOCKET_URL,
    dispatch,
    reset
  );

  return { socket, readyState };
}

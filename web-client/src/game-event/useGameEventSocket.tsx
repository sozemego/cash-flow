import { GAME_EVENT_SERVICE_SOCKET_URL } from "../config/urls";
import { useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";
import { gameEventInit } from "./actions";
import { useCallback } from "react";

export function useGameEventSocket() {
  const dispatch = useDispatch();

  const reset = useCallback(() => dispatch(gameEventInit()), [dispatch]);

  const { socket, readyState } = useWebsocket(
    GAME_EVENT_SERVICE_SOCKET_URL,
    dispatch,
    reset
  );

  return { socket, readyState };
}

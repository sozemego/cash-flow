import { TRUCK_SERVICE_SOCKET_URL } from "../config/urls";
import { useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";
import { truckInit } from "./actions";
import { useCallback } from "react";

export function useTruckSocket() {
  const dispatch = useDispatch();

  const reset = useCallback(() => dispatch(truckInit()), [dispatch]);

  const { socket, readyState } = useWebsocket(
    TRUCK_SERVICE_SOCKET_URL,
    dispatch,
    reset
  );

  return { socket, readyState };
}

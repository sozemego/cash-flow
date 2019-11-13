import { TRUCK_SERVICE_SOCKET_URL } from "../config/urls";
import { useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";
import { truckInit } from "./actions";
import { useCallback } from "react";
import { useGetUser } from "../auth/selectors";

export function useTruckSocket() {
  const dispatch = useDispatch();
  const user = useGetUser();
  const token = user ? user.token : '';

  const reset = useCallback(() => dispatch(truckInit()), [dispatch]);


  const { socket, readyState } = useWebsocket(
    TRUCK_SERVICE_SOCKET_URL + `?token=${token}`,
    dispatch,
    reset
  );

  return { socket, readyState };
}

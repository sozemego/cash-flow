import { TRUCK_SERVICE_URL } from "../config/urls";
import { useWebsocket } from "../websocket/hook";
import { useDispatch } from "react-redux";

export function useTruckSocket() {
	const dispatch = useDispatch();

	const { socket, readyState } = useWebsocket(
		TRUCK_SERVICE_URL + "/websocket",
		dispatch
	);

	return { socket, readyState };
}

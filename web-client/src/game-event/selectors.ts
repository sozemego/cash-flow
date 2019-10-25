import { useSelector } from "react-redux";
import { AppState } from "../store";

export function useGetEvents() {
    return useSelector((state: AppState) => state.gameEvent.events);
}
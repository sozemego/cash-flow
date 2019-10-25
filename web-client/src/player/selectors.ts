import { useSelector } from "react-redux";
import { AppState } from "../store";

export function useGetPlayer() {
  return useSelector((state: AppState) => state.player.player);
}

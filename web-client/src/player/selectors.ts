import { useSelector } from "react-redux";
import { AppState } from "../store";

export function getPlayer(state: AppState) {
  return state.player.player;
}

export function useGetPlayer() {
  return useSelector((state: AppState) => state.player.player);
}

export function useGetCompetitors() {
  return useSelector((state: AppState) => state.player.competitors);
}

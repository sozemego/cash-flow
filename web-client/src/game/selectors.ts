import { useSelector } from "react-redux";
import { AppState } from "../store";

export function useGetSelectedSections() {
  return useSelector((state: AppState) => state.game.selectedSections);
}

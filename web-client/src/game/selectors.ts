import { useSelector } from "react-redux";
import { AppState } from "../store";

export function useGetSelectedSections() {
  return useSelector((state: AppState) => state.game.selectedSections);
}

export function useGetSelectedCityId() {
  return useSelector((state: AppState) => state.game.selectedCity);
}

export function useGetSelectedTruckId() {
  return useSelector((state: AppState) => state.game.selectedTruck);
}
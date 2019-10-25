import { useSelector } from "react-redux";
import { CityMap, ResourceMap } from "./index";
import { AppState } from "../store";

export function useGetCities(): CityMap {
  return useSelector((state: AppState) => state.world.cities);
}

export function useGetHighlightedCity() {
  return useSelector((state: AppState) => state.world.highlightedCity);
}

export function useGetResources(): ResourceMap {
  return useSelector((state: AppState) => state.world.resources);
}

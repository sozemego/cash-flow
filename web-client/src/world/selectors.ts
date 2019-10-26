import { useSelector } from "react-redux";
import { CityMap, ResourceMap } from "./index";
import { AppState } from "../store";

function getCitiesSelector(state: AppState) {
  return state.world.cities;
}

export function getCities(state: AppState) {
  return getCitiesSelector(state);
}

export function useGetCities(): CityMap {
  return useSelector(getCitiesSelector);
}

export function useGetHighlightedCity() {
  return useSelector((state: AppState) => state.world.highlightedCity);
}

export function useGetResources(): ResourceMap {
  return useSelector((state: AppState) => state.world.resources);
}

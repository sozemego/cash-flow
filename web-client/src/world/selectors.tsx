import { useSelector } from "react-redux";
import { ResourceMap } from "./reducer";

export function useGetCities() {
  return useSelector((state: any) => state.world.cities);
}

export function useGetHighlightedCity() {
  return useSelector((state: any) => state.world.highlightedCity);
}

export function useGetResources(): ResourceMap {
  return useSelector((state: any) => state.world.resources);
}

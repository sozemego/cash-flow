import { useSelector } from "react-redux";

export function useGetCities() {
  return useSelector(state => state.world.cities);
}

export function useGetHighlightedCity() {
  return useSelector(state => state.world.highlightedCity);
}

export function useGetResources() {
  return useSelector(state => state.world.resources);
}
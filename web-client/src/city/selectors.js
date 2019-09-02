import { useSelector } from "react-redux";

export function useGetCities() {
  return useSelector(state => state.city.cities);
}

export function useGetHighlightedCity() {
  return useSelector(state => state.city.highlightedCity);
}

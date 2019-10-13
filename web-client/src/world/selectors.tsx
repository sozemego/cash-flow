import { useSelector } from "react-redux";
import { ResourceMap } from "./reducer";
import { ICity } from "./index.d";

export interface CityMap {
  [id: string]: ICity;
}

export function useGetCities(): CityMap {
  return useSelector((state: any) => state.world.cities);
}

export function useGetHighlightedCity() {
  return useSelector((state: any) => state.world.highlightedCity);
}

export function useGetResources(): ResourceMap {
  return useSelector((state: any) => state.world.resources);
}

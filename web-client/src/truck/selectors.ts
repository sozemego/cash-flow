import { useSelector } from "react-redux";
import { AppState } from "../store";
import { TruckMap } from "./index";

export function getTrucks(state: AppState) {
  return state.truck.trucks;
}

export function useGetTrucks(): TruckMap;
export function useGetTrucks(cityId: string): TruckMap;

export function useGetTrucks(cityId?: string) {
  const trucks = useSelector(getTrucks);
  if (!cityId) {
    return trucks;
  }
  const trucksInCity = {...trucks};
  for (let trucksKey in trucks) {
    const truck = trucks[trucksKey];
    if (truck.navigation.currentCityId !== cityId) {
      delete trucksInCity[trucksKey];
    }
  }
  return trucksInCity;
}

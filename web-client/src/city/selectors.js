import { useSelector } from "react-redux";

export function useGetCities() {
  return useSelector(state => state.city.cities);
}

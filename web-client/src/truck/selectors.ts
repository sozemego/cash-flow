/* eslint react-hooks/rules-of-hooks: 1 */
import { useSelector } from "react-redux";
import { AppState } from "../store";


export function getTrucks(state: AppState) {
  return state.truck.trucks;
}

export function useGetTrucks() {
  return useSelector(getTrucks);
}

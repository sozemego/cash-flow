import { useSelector } from "react-redux";
import { AppState } from "../store";

export function useGetTrucks() {
  const trucks = useSelector((state: AppState) => {
    return state.truck.trucks;
  });
  return trucks;
}

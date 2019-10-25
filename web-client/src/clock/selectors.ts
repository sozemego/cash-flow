import { useSelector } from "react-redux";
import { AppState } from "../store";

export function useGetGameClock() {
  const multiplier = useSelector((state: AppState) => state.clock.multiplier);
  const startTime = useSelector((state: AppState) => state.clock.startTime);
  return { multiplier, startTime };
}

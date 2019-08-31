import { useSelector } from "react-redux";

export function useGetClock() {
  const multiplier = useSelector(state => state.clock.multiplier);
  const startTime = useSelector(state => state.clock.startTime);
  return { multiplier, startTime };
}

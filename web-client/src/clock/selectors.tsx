import { useSelector } from "react-redux";

export function useGetGameClock() {
  const multiplier = useSelector((state: any) => state.clock.multiplier);
  const startTime = useSelector((state: any) => state.clock.startTime);
  return { multiplier, startTime };
}

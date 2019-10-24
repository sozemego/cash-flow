import { useState, useEffect } from "react";
import { RealClockHook, UseRealClockInitial } from "./index";

export function useRealClock({ interval }: UseRealClockInitial): RealClockHook {
  const [time, setTime] = useState(Date.now());

  useEffect(() => {
    let intervalId = setInterval(() => {
      setTime(time + interval);
    }, interval);
    return () => clearInterval(intervalId);
  }, [time, interval]);

  return { time: Date.now() };
}

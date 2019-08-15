import { useState, useEffect } from "react";

export function useClock({interval}) {
  const [time, setTime] = useState(Date.now());

  useEffect(() => {
    let intervalId = setInterval(() => {
    	setTime(time + interval);
		}, interval);
    return () => clearInterval(intervalId);
  }, [time, interval]);

  return { time };
}

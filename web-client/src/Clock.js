import React, { useEffect, useState } from "react";
import { useClock } from "./hooks/clock";

function format(num) {
  if (num > 9) {
    return "" + num;
  }
  if (num < 10) {
    return "0" + num;
  }
}

export function Clock() {
  const [clock, setClock] = useState({ multiplier: 1, startTime: Date.now() });
  const { multiplier, startTime } = clock;

  useEffect(() => {
    fetch("http://localhost:9004/clock/")
      .then(result => result.json())
      .then(json => setClock(json));
  }, []);

  const { time } = useClock({ interval: 1000 });
  const timePassed = time - startTime;
  const currentTime = time + (timePassed * multiplier);

  const date = new Date(currentTime);
  const hour = date.getHours();
  const minute = date.getMinutes();

  const dayOfMonth = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();

  return (
    <div>
      {`${format(dayOfMonth)}-${format(month)}-${format(year)} ${format(hour)}:${format(minute)}`}
      <hr />
    </div>
  );
}

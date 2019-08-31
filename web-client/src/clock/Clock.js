import React, { useEffect } from "react";
import { useClock } from "../hooks/clock";
import { useGetClock } from "./selectors";
import { useDispatch } from "react-redux";
import { CLOCK_FETCHED } from "./actions";

function format(num) {
  if (num > 9) {
    return "" + num;
  }
  if (num < 10) {
    return "0" + num;
  }
}

export function Clock() {
  const clock = useGetClock();
  const dispatch = useDispatch();
  const { multiplier, startTime } = clock;

  useEffect(() => {
    fetch("http://localhost:9004/clock/")
      .then(result => result.json())
      .then(json => dispatch({type: CLOCK_FETCHED, clock: json}));
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

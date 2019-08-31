import React, { useEffect } from "react";
import { useClock } from "../hooks/clock";
import { useGetClock } from "./selectors";
import { useDispatch } from "react-redux";
import { CLOCK_FETCHED } from "./actions";
import { getCurrentGameDate, getFormattedDateTime } from "./business";

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

  const date = getCurrentGameDate(clock);

  return (
    <div>
      {getFormattedDateTime(date)}
    </div>
  );
}

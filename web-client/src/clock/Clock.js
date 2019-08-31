import React from "react";
import { useRealClock } from "./realClock";
import { useGetGameClock } from "./selectors";
import { getCurrentGameDate, getFormattedDateTime } from "./business";

export function Clock() {
  const clock = useGetGameClock();

  useRealClock({ interval: 1000 });

  const date = getCurrentGameDate(clock);

  return <div>{getFormattedDateTime(date)}</div>;
}

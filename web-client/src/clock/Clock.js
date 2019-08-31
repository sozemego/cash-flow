import React from "react";
import { getCurrentGameDate, getFormattedDateTime } from "./business";
import { useGameClock } from "./gameClock";

export function Clock() {
	const {clock} = useGameClock({interval: 1000});

	const date = getCurrentGameDate(clock);

  return <div>{getFormattedDateTime(date)}</div>;
}

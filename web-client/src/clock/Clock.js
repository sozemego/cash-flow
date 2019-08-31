import React from "react";
import { getCurrentGameDate, getFormattedDateTime } from "./business";
import { useGameClock } from "./gameClock";

export function Clock() {
	const { clock } = useGameClock({interval: 1000});

	const date = getCurrentGameDate(clock);
	console.log('current game time from Clock.js', date);

  return <div>{getFormattedDateTime(date)}</div>;
}

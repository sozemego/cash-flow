import { useEffect } from "react";
import { useGetGameClock } from "./selectors";
import { useRealClock } from "./realClock";
import { CLOCK_FETCHED } from "./actions";
import { useDispatch } from "react-redux";

export function useGameClock({ interval }) {
	const clock = useGetGameClock();
	const dispatch = useDispatch();
	useEffect(() => {
		if (!clock.multiplier) {
			fetch("http://localhost:9004/clock/")
				.then(result => result.json())
				.then(json => dispatch({ type: CLOCK_FETCHED, clock: json }));
		}
	}, [ clock.multiplier ]);
	const { time } = useRealClock({ interval });
	const realTimePassed = time - clock.startTime;
	const gameTimePassed = realTimePassed * clock.multiplier;
	return { clock };
}

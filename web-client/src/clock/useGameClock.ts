import { useEffect } from "react";
import { useGetGameClock } from "./selectors";
import { useRealClock } from "./useRealClock";
import { CLOCK_FETCHED } from "./actions";
import { useDispatch } from "react-redux";
import { getCurrentGameDate } from "./business";
import { GameClockHook, UseGameClockInitial } from "./index";

export function useGameClock({ interval }: UseGameClockInitial): GameClockHook {
  const clock = useGetGameClock();
  const dispatch = useDispatch();

  useEffect(() => {
    if (!clock.multiplier) {
      fetch("http://localhost:9004/clock/")
        .then(result => result.json())
        .then(json => dispatch({ type: CLOCK_FETCHED, clock: json }));
    }
  }, [clock.multiplier, dispatch]);

  const { time } = useRealClock({ interval });
  const gameDate = getCurrentGameDate(clock);
  return { clock, date: gameDate, time: gameDate.getTime(), realTime: time };
}

import { useEffect } from "react";
import { useGetGameClock } from "./selectors";
import { useRealClock } from "./useRealClock";
import { CLOCK_FETCHED } from "./actions";
import { useDispatch } from "react-redux";
import { getCurrentGameDate } from "./business";
import { GameClockHook, UseGameClockInitial } from "./index";
import { CLOCK_SERVICE_CLOCK_URL } from "../config/urls";
import { useGetUser } from "../auth/selectors";
import { getAsJson } from "../rest/client";

export function useGameClock({ interval }: UseGameClockInitial): GameClockHook {
  const clock = useGetGameClock();
  const dispatch = useDispatch();
  const user = useGetUser();

  useEffect(() => {
    if (!clock.multiplier && user) {
      getAsJson(CLOCK_SERVICE_CLOCK_URL)
        .then(json => dispatch({ type: CLOCK_FETCHED, clock: json }));
    }
  }, [clock.multiplier, dispatch, user]);

  const { time } = useRealClock({ interval });
  const gameDate = getCurrentGameDate(clock);
  return { clock, date: gameDate, time: gameDate.getTime(), realTime: time };
}

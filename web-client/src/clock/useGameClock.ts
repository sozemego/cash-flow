import { useEffect } from "react";
import { useGetGameClock } from "./selectors";
import { useRealClock } from "./useRealClock";
import { clockFetched } from "./actions";
import { useDispatch } from "react-redux";
import { getCurrentGameDate } from "./business";
import { GameClockHook, IClock, UseGameClockInitial } from "./index";
import { CLOCK_SERVICE_CLOCK_URL } from "../config/urls";
import { useGetUser } from "../auth/selectors";
import { getAsJson } from "../rest/client";

export function useGameClock({ interval }: UseGameClockInitial): GameClockHook {
  const clock = useGetGameClock();
  const dispatch = useDispatch();
  const user = useGetUser();

  useEffect(() => {
    if (!clock.multiplier && user) {
      getAsJson<IClock>(CLOCK_SERVICE_CLOCK_URL)
        .then(response => dispatch(clockFetched(response.payload)));
    }
  }, [clock.multiplier, dispatch, user]);

  const { time } = useRealClock({ interval });
  const gameDate = getCurrentGameDate(clock);
  return { clock, date: gameDate, time: gameDate.getTime(), realTime: time };
}

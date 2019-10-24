import { produce } from "immer";
import { CLOCK_FETCHED } from "./actions";
import { ClockActions, ClockFetchedAction, ClockState } from "./index";

const initialState: ClockState = {
  multiplier: 0,
  startTime: Date.now()
};

export function reducer(
  state: ClockState = initialState,
  action: ClockActions
): ClockState {
  switch (action.type) {
    case CLOCK_FETCHED:
      return clockFetched(state, action);
    default:
      break;
  }
  return state;
}

const clockFetched = produce(
  (state: ClockState, action: ClockFetchedAction) => {
    const { clock } = action;
    const { multiplier, startTime } = clock;
    state.multiplier = multiplier;
    state.startTime = startTime;
  }
);

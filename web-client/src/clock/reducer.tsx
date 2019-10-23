import { produce } from "immer";
import { CLOCK_FETCHED } from "./actions";

export interface ClockState {
  multiplier: number;
  startTime: number;
}

const initialState: ClockState = {
  multiplier: 0,
  startTime: Date.now()
};

export function reducer(state: ClockState = initialState, action) {
  switch (action.type) {
    case CLOCK_FETCHED:
      return clockFetched(state, action);
    default:
      break;
  }
  return state;
}

const clockFetched = produce((state, action) => {
  const { clock } = action;
  const { multiplier, startTime } = clock;
  state.multiplier = multiplier;
  state.startTime = startTime;
});

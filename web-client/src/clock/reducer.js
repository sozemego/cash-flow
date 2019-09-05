import { produce } from "immer";
import { CLOCK_FETCHED } from "./actions";

const initialState = {
  multiplier: 0,
  startTime: Date.now()
};

export function reducer(state = initialState, action) {
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

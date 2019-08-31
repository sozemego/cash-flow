import { CLOCK_FETCHED } from "./actions";

const initialState = {
  multiplier: 0,
  startTime: Date.now()
};

export function reducer(state = initialState, action) {
	switch (action.type) {
		case CLOCK_FETCHED: return clockFetched(state, action);
	}
	return state;
}

function clockFetched(state, action) {
  const { clock } = action;
  const { multiplier, startTime } = clock;
  return { ...state, multiplier, startTime };
}

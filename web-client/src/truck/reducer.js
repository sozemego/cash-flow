import { TRUCK_ADDED } from "./actions";

const initialState = {
  trucks: []
};

export function reducer(state = initialState, action) {
  switch (action.type) {
    case TRUCK_ADDED:
      return truckAdded(state, action);
    default:
      return state;
  }
}

function truckAdded(state, action) {
  const trucks = [...state.trucks, action.truck];
  return { ...state, trucks };
}

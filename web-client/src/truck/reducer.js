import { TRUCK_ADDED, TRUCK_TRAVEL_STARTED } from "./actions";

const initialState = {
  trucks: {}
};

export function reducer(state = initialState, action) {
  switch (action.type) {
    case TRUCK_ADDED:
      return truckAdded(state, action);
    case TRUCK_TRAVEL_STARTED:
      return truckTravelStarted(state, action);
    default:
      return state;
  }
}

function truckAdded(state, action) {
  const trucks = { ...state.trucks };
  trucks[action.truck.id] = action.truck;
  return { ...state, trucks };
}

function truckTravelStarted(state, action) {
  const { truckId, targetCityId, travelStartTime, travelArrivalTime } = action;
  const trucks = { ...state.trucks };
  const truck = { ...trucks[truckId] };

  return { ...state, trucks };
}

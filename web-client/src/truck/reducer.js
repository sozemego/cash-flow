import produce from "immer";
import {
  STORAGE_CONTENT_CHANGED,
  TRUCK_ADDED,
  TRUCK_ARRIVED,
  TRUCK_TRAVEL_STARTED
} from "./actions";

const initialState = {
  trucks: {}
};

export function reducer(state = initialState, action) {
  switch (action.type) {
    case TRUCK_ADDED:
      return truckAdded(state, action);
    case TRUCK_TRAVEL_STARTED:
      return truckTravelStarted(state, action);
    case TRUCK_ARRIVED:
      return truckArrived(state, action);
    case STORAGE_CONTENT_CHANGED:
      return storageContentChanged(state, action);
    default:
      return state;
  }
}

const truckAdded = produce((state, action) => {
	state.trucks[action.truck.id] = action.truck;
});

const truckTravelStarted = produce((state, action) => {
	const { truckId, nextCityId, startTime, arrivalTime } = action;
	const truck = state.trucks[truckId];
	truck.navigation = {
		...truck.navigation,
		nextCityId,
		startTime,
		arrivalTime
	};
});

const truckArrived = produce((state, action) => {
	const { truckId } = action;
	const { navigation } = state.trucks[truckId];
	navigation.currentCityId = navigation.nextCityId;
	navigation.startTime = -1;
	navigation.arrivalTime = -1;
	navigation.nextCityId = null;
});

const storageContentChanged = produce((state, action) => {
	const { entityId, resource, change } = action;
	const truck = state.trucks[entityId];
	if (!truck) {
		return;
	}
	const actualCount = truck.storage.resources[resource] || 0;
	truck.storage.resources[resource] = actualCount + change;
});

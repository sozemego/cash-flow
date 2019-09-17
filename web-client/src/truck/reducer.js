import produce from "immer";
import {
  STORAGE_CAPACITY_CHANGED,
  STORAGE_CONTENT_CHANGED,
  TRUCK_ADDED,
  TRUCK_ARRIVED,
  TRUCK_TRAVEL_STARTED
} from "./actions";
import { transfer } from "../storage/business";

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
    case STORAGE_CAPACITY_CHANGED:
      return storageCapacityChanged(state, action);
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

const storageCapacityChanged = produce((state, action) => {
  const { entityId, change } = action;
  const truck = state.trucks[entityId];
  if (!truck) {
    return;
  }
  const oldStorage = truck.storage;
  const nextStorage = { ...truck.storage };
  nextStorage.capacity = oldStorage.capacity + change;
  transfer(oldStorage, nextStorage);
  truck.storage = nextStorage;
});

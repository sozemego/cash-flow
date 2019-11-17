import produce from "immer";
import {
  STORAGE_CAPACITY_CHANGED,
  STORAGE_CONTENT_CHANGED,
  TRUCK_ADDED,
  TRUCK_ARRIVED,
  TRUCK_INIT,
  TRUCK_TRAVEL_STARTED
} from "./actions";
import { transfer } from "../storage/business";
import {
  StorageCapacityChanged,
  TruckActions,
  TruckAddedAction,
  TruckArrivedAction,
  TruckState,
  TruckTravelStartedAction
} from "./index";
import { StorageContentChangedAction } from "../storage";
import { USER_LOGGED_OUT } from "../auth/actions";

const initialState: TruckState = {
  trucks: {}
};

export function reducer(
  state = initialState,
  action: TruckActions
): TruckState {
  switch (action.type) {
    case TRUCK_INIT:
      return initialState;
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
    case USER_LOGGED_OUT:
      return initialState;
    default:
      return state;
  }
}

const truckAdded = produce((state: TruckState, action: TruckAddedAction) => {
  state.trucks[action.truck.id] = action.truck;
});

const truckTravelStarted = produce(
  (state: TruckState, action: TruckTravelStartedAction) => {
    const { truckId, nextCityId, startTime, arrivalTime } = action;
    const truck = state.trucks[truckId];
    truck.navigation = {
      ...truck.navigation,
      nextCityId,
      startTime,
      arrivalTime
    };
  }
);

const truckArrived = produce(
  (state: TruckState, action: TruckArrivedAction) => {
    const { truckId } = action;
    const { navigation } = state.trucks[truckId as string];
    const { nextCityId } = navigation;
    if (nextCityId != null) {
      navigation.currentCityId = nextCityId;
      navigation.startTime = -1;
      navigation.arrivalTime = -1;
      navigation.nextCityId = null;
    }
  }
);

const storageContentChanged = produce(
  (state: TruckState, action: StorageContentChangedAction) => {
    const { entityId, resource, change } = action;
    const truck = state.trucks[entityId];
    if (!truck) {
      return;
    }
    const actualCount = truck.storage.resources[resource] || 0;
    const newCount = actualCount + change;
    if (newCount === 0) {
      delete truck.storage.resources[resource];
    } else {
      truck.storage.resources[resource] = actualCount + change;
    }
  }
);

const storageCapacityChanged = produce(
  (state: TruckState, action: StorageCapacityChanged) => {
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
  }
);

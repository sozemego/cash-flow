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

function truckAdded(state, action) {
  const trucks = { ...state.trucks };
  trucks[action.truck.id] = action.truck;
  return { ...state, trucks };
}

function truckTravelStarted(state, action) {
  const { truckId, nextCityId, startTime, arrivalTime } = action;
  const trucks = { ...state.trucks };
  const truck = { ...trucks[truckId] };
  truck.navigation = {
    ...truck.navigation,
    nextCityId,
    startTime,
    arrivalTime
  };
  trucks[truck.id] = truck;
  return { ...state, trucks };
}

function truckArrived(state, action) {
  const { truckId } = action;
  const trucks = { ...state.trucks };
  const truck = { ...trucks[truckId] };
  const navigation = { ...truck.navigation };
  navigation.currentCityId = navigation.nextCityId;
  navigation.startTime = -1;
  navigation.arrivalTime = -1;
  navigation.nextCityId = null;
  truck.navigation = navigation;
  trucks[truckId] = truck;
  return { ...state, trucks };
}

function storageContentChanged(state, action) {
  const { entityId, resource, change } = action;
  const trucks = { ...state.trucks };
  const hasTruck = !!trucks[entityId];
  if (!hasTruck) {
    return state;
  }
  const truck = { ...trucks[entityId] };
  const storage = { ...truck.storage };
  const resources = { ...storage.resources };
  const actualCount = resources[resource];
  resources[resource] = actualCount + change;
  storage.resources = resources;
  truck.storage = storage;
  trucks[entityId] = truck;

  return { ...state, trucks };
}

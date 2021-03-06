import { makeActionCreator } from "../store/actionCreator";

export const TRUCK_INIT = "TRUCK_INIT";
export const truckInit = makeActionCreator(TRUCK_INIT);

export const TRUCK_ADDED = "TRUCK_ADDED";
export const TRUCK_TRAVEL_STARTED = "TRUCK_TRAVEL_STARTED";
export const TRUCK_ARRIVED = "TRUCK_ARRIVED";
export const STORAGE_CONTENT_CHANGED = "STORAGE_CONTENT_CHANGED";
export const STORAGE_CAPACITY_CHANGED = "STORAGE_CAPACITY_CHANGED";

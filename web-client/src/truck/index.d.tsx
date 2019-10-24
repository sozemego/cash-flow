import {
  STORAGE_CAPACITY_CHANGED,
  STORAGE_CONTENT_CHANGED,
  TRUCK_ADDED,
  TRUCK_ARRIVED,
  TRUCK_TRAVEL_STARTED
} from "./actions";
import { ResourceName } from "../world/index.d";
import { IStorage } from "../storage/index.d";

export interface TruckState {
  trucks: TruckMap;
}

export interface TruckMap {
  [id: string]: ITruck;
}

export interface ITruck {
  id: string;
  navigation: INavigation;
  storage: IStorage;
}

export interface INavigation {
  currentCityId: string;
  startTime: number;
  arrivalTime: number;
  nextCityId: string | null;
}

export interface TruckTravelStartedAction {
  type: typeof TRUCK_TRAVEL_STARTED;
  truckId: string;
  nextCityId: string;
  startTime: number;
  arrivalTime: number;
}

export interface TruckAddedAction {
  type: typeof TRUCK_ADDED;
  truck: ITruck;
}

export interface TruckArrivedAction {
  type: typeof TRUCK_ARRIVED;
  truckId: string;
}

export interface StorageContentChangedAction {
  type: typeof STORAGE_CONTENT_CHANGED;
  entityId: string;
  resource: ResourceName;
  change: number;
}

export interface StorageCapacityChanged {
  type: typeof STORAGE_CAPACITY_CHANGED;
  entityId: string;
  resource: ResourceName;
  change: number;
}

export type TruckActions =
  | TruckTravelStartedAction
  | TruckAddedAction
  | TruckArrivedAction
  | StorageContentChangedAction
  | StorageCapacityChanged;

import {
  STORAGE_CAPACITY_CHANGED,
  TRUCK_ADDED,
  TRUCK_ARRIVED,
  TRUCK_TRAVEL_STARTED
} from "./actions";
import { ResourceName } from "../world";
import { IStorage, StorageContentChangedAction } from "../storage";

export interface TruckState {
  trucks: TruckMap;
}

export interface TruckProps {
  truck: ITruck;
}

export interface TravelToProps {
  truck: ITruck;
}

export interface TravellingProps {
  truck: ITruck;
}

export interface BuyProps {
  truck: ITruck;
  cityId: string;
}

export interface FactoryResourceProps {
  truck: ITruck;
  resource: ResourceName;
  count: number;
  price: number;
  factoryId: string;
}

export interface TruckListProps {
  trucks: ITruck[]
}

export interface TruckMap {
  [id: string]: ITruck;
}

export interface ITruck {
  id: string;
  name: string;
  speed: number;
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
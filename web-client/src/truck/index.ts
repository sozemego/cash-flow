import {
  STORAGE_CAPACITY_CHANGED,
  TRUCK_ADDED,
  TRUCK_ARRIVED,
  TRUCK_INIT,
  TRUCK_TRAVEL_STARTED
} from "./actions";
import { ResourceName } from "../world";
import { IStorage, StorageContentChangedAction } from "../storage";
import { IFactory, InputOutput } from "../factory";
import { UserLoggedOut } from "../auth";

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

export interface TradeProps {
  truck: ITruck;
  cityId: string;
}

export interface FactoryResourceProps {
  truck: ITruck;
  resource: ResourceName;
  count: number;
  price: number;
  factory: IFactory;
  type: InputOutput;
}

export interface TruckListProps {
  trucks: ITruck[];
}

export interface TruckIconProps {
  texture: string;
}

export interface TruckMap {
  [id: string]: ITruck;
}

export interface ITruck {
  id: string;
  name: string;
  texture: string;
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

export interface TruckInitAction {
  type: typeof TRUCK_INIT;
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
  | TruckInitAction
  | TruckTravelStartedAction
  | TruckAddedAction
  | TruckArrivedAction
  | StorageContentChangedAction
  | StorageCapacityChanged
  | UserLoggedOut;

export interface ResourceFromFactory {
  factory: IFactory;
  resource: string;
  count: number;
  price: number;
}
export interface TruckMapTooltipProps {
  truck: ITruck;
}

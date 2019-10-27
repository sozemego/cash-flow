import { ICity, ResourceName } from "../world";
import {
  FACTORY_ADDED,
  FACTORY_INIT,
  PRODUCTION_FINISHED,
  PRODUCTION_LINE_ADDED,
  PRODUCTION_STARTED,
  RESOURCE_PRICE_CHANGED,
  RESOURCE_PRODUCED,
  RESOURCE_PRODUCTION_STARTED,
  RESOURCE_SOLD,
  RESOURCE_STORAGE_CAPACITY_CHANGED
} from "./actions";
import {
  IResourceCount,
  ResourceCounts,
  StorageContentChangedAction
} from "../storage";

export interface FactoryState {
  factories: IFactory[];
}

export interface IFactory {
  id: string;
  name: string;
  texture: string;
  cityId: string;
  storage: IFactoryStorage;
  producer: Producer;
}

export type IFactoryStorage = { [K in ResourceName]?: IStorageSlot };

export interface IStorageSlot {
  resource: ResourceName;
  count: number;
  capacity: number;
  price: number;
}

export type IStorageSlotEntry = [ResourceName, IStorageSlot];

export type ProducerEntry = [string, number];

export type ProducerInput = {
  [K in ResourceName]: number;
};

export type ProducerOutput = ProducerInput;

export interface Producer {
  input: ProducerInput;
  output: ProducerOutput;
  time: number;
  progress: number;
  producing: boolean;
  productionStartTime: number;
}

export interface FactoryProps {
  factory: IFactory;
}

export interface FactoryEventsProps {
  factory: IFactory;
  showEvents: boolean;
  onClose: (e: React.MouseEvent<HTMLElement>) => void;
}

export interface FactoryByCityProps {
  factories: IFactory[];
  cities: ICity[];
}

export interface ProducerInputOutputProps {
  input: ProducerInput;
  output: ProducerOutput;
}

export type InputOutput = "INPUT" | "OUTPUT";

export interface ResourceCountProps {
  resourceCount: IResourceCount;
  input: InputOutput;
}

export interface BaseFactoryEvent {
  timestamp: number[];
}

export type FactoryEvent = BaseFactoryEvent;

export interface FactoryInitAction {
  type: typeof FACTORY_INIT;
}

export interface FactoryAddedAction {
  type: typeof FACTORY_ADDED;
  factoryDTO: IFactory;
}

export interface ResourceProducedAction {
  type: typeof RESOURCE_PRODUCED;
  factoryId: string;
  resource: ResourceName;
}

export interface ResourceStorageCapacityChanged {
  type: typeof RESOURCE_STORAGE_CAPACITY_CHANGED;
  entityId: string;
  capacityChanges: ResourceCounts;
}

export interface ProductionFinishedAction {
  type: typeof PRODUCTION_FINISHED;
  entityId: string;
}

export interface ProductionStarted {
  type: typeof PRODUCTION_STARTED;
  entityId: string;
  productionStartTime: number;
}

export interface ResourceSoldAction {
  type: typeof RESOURCE_SOLD;
  entityId: string;
  resource: ResourceName;
  count: number;
}

export interface ResourcePriceChangedAction {
  type: typeof RESOURCE_PRICE_CHANGED;
  entityId: string;
  prices: ResourceCounts;
}

export interface ProductionLineAddedAction {
  type: typeof PRODUCTION_LINE_ADDED;
  entityId: string;
  input: ProducerInput;
  output: ProducerOutput;
  time: number;
}

export type FactoryAction =
  | FactoryInitAction
  | FactoryAddedAction
  | StorageContentChangedAction
  | ResourceStorageCapacityChanged
  | ProductionFinishedAction
  | ProductionStarted
  | ResourceSoldAction
  | ProductionLineAddedAction
  | ResourcePriceChangedAction;

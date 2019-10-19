import { ResourceName } from "../world/index.d";

export interface IFactory {
  id: string;
  name: string;
  texture: string;
  cityId: string;
  storage: IFactoryStorage;
  producer: Producer;
}

// type IFactoryStorage2 = { [K in ResourceName]?: IStorageSlot }

export type IFactoryStorage = { [K in ResourceName]?: IStorageSlot };

export interface IStorageSlot {
  resource: ResourceName;
  count: number;
  capacity: number;
  price: number;
}

export type IStorageSlotEntry = [ResourceName, IStorageSlot];

export interface Producer {
  resource: ResourceName;
  time: number;
  progress: number;
  producing: boolean;
  productionStartTime: number;
}

export interface BaseFactoryEvent {
  timestamp: number[];
}

export type FactoryEvent = BaseFactoryEvent;

export interface IFactory {
  id: string;
  name: string;
  texture: string;
  cityId: string;
  storage: IFactoryStorage;
  producer: Producer;
}

export interface IFactoryStorage {
  [key: string]: IStorageSlot;
}

export interface IStorageSlot {
  resource: string;
  count: number;
  capacity: number;
  price: number;
}

export type IStorageSlotEntry = [string, IStorageSlot];

export interface Producer {
  resource: string;
  time: number;
  progress: number;
  producing: boolean;
  productionStartTime: number;
}

export interface BaseFactoryEvent {
  timestamp: number[];
}

export type FactoryEvent = BaseFactoryEvent;

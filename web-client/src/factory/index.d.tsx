export interface IFactory {
  id: string;
  name: string;
  texture: string;
  cityId: string;
  storage: IFactoryStorage;
  producer: Producer;
}

export interface Resources {
  [key: string]: number;
}

export interface Capacities {
  [key: string]: number;
}

export interface IFactoryStorage {
  capacities: Capacities;
  resources: Resources;
}

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

import { produce } from "immer";
import {
  FACTORY_ADDED,
  PRODUCTION_FINISHED,
  PRODUCTION_STARTED,
  RESOURCE_PRODUCED,
  RESOURCE_PRODUCTION_STARTED,
  RESOURCE_SOLD,
  RESOURCE_STORAGE_CAPACITY_CHANGED,
  STORAGE_CAPACITY_CHANGED,
  STORAGE_CONTENT_CHANGED
} from "./actions";
import { Action } from "../store/actionCreator";
import { IFactory, IFactoryStorage } from "./index.d";
import {transfer} from "./FactoryStorage";

export interface FactoryState {
  factories: IFactory[];
}

const initialState: FactoryState = {
  factories: []
};

export function reducer(state: FactoryState = initialState, action: Action) {
  switch (action.type) {
    case FACTORY_ADDED:
      return factoryAdded(state, action);
    case RESOURCE_PRODUCED:
      return resourceProduced(state, action);
    case RESOURCE_PRODUCTION_STARTED:
      return resourceProductionStarted(state, action);
    case STORAGE_CONTENT_CHANGED:
      return storageContentChanged(state, action);
    case STORAGE_CAPACITY_CHANGED:
      return storageCapacityChanged(state, action);
    case RESOURCE_STORAGE_CAPACITY_CHANGED:
      return resourceStorageCapacityChanged(state, action);
    case PRODUCTION_FINISHED:
      return productionFinished(state, action);
    case PRODUCTION_STARTED:
      return productionStarted(state, action);
    case RESOURCE_SOLD:
      return resourceSold(state, action);
    default:
      return state;
  }
}

const factoryAdded = produce((state, action) => {
  const { factoryDTO } = action;
  state.factories.push(factoryDTO);
});

const productionFinished = produce((state, action) => {
  const { entityId } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return state;
  }
  const { producer, storage } = factory;
  const { resource } = producer;
  const count = storage.resources[resource] || 0;
  storage.resources[resource] = count + 1;
  factory.producer.productionStartTime = -1;
});

const productionStarted = produce((state, action) => {
  const { entityId, productionStartTime } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return state;
  }
  factory.producer.productionStartTime = productionStartTime;
});

const resourceProduced = produce((state, action) => {
  const { factoryId, resource } = action;
  const factory = findFactory(state, factoryId);
  if (!factory) {
    return state;
  }
  const { storage } = factory;
  const count = storage.resources[resource] || 0;
  storage.resources[resource] = count + 1;
  factory.producer.productionStartTime = -1;
});

const resourceProductionStarted = produce((state, action) => {
  const { factoryId, productionStartTime } = action;
  const factory = findFactory(state, factoryId);
  if (!factory) {
    return state;
  }
  factory.producer.productionStartTime = productionStartTime;
});

const storageContentChanged = produce((state, action) => {
  const { entityId, resource, change } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return;
  }
  const currentCount = factory.storage.resources[resource] || 0;
  factory.storage.resources[resource] = currentCount + change;
});

const storageCapacityChanged = produce((state, action) => {
  //no longer in use
  return state;
});

const resourceStorageCapacityChanged = produce((state, action) => {
  const { entityId, capacityChanges } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return;
  }
  const { storage } = factory;
  const newStorage: IFactoryStorage = {
    capacities: storage.capacities,
    resources: {}
  };
  Object.entries(capacityChanges).forEach(([resource, change]) => {
    const capacity = newStorage.capacities[resource] || 0;
    newStorage.capacities[resource] = capacity + (change as number);
  });
  transfer(storage, newStorage);
});

const resourceSold = produce((state, action) => {
  const { entityId, resource, count } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return;
  }
  const { storage } = factory;
  const oldCount: number = storage.resources[resource];
  storage.resources[resource] = oldCount - count;
});

function findFactory(
  state: FactoryState,
  factoryId: string
): IFactory | undefined {
  return state.factories.find(factory => factory.id === factoryId);
}

import { produce } from "immer";
import {
  FACTORY_ADDED,
  PRODUCTION_FINISHED,
  PRODUCTION_LINE_ADDED,
  PRODUCTION_STARTED,
  RESOURCE_PRICE_CHANGED,
  RESOURCE_PRODUCED,
  RESOURCE_PRODUCTION_STARTED,
  RESOURCE_SOLD,
  RESOURCE_STORAGE_CAPACITY_CHANGED,
  STORAGE_CAPACITY_CHANGED,
  STORAGE_CONTENT_CHANGED
} from "./actions";
import { Action } from "../store/actionCreator";
import { IFactory, IFactoryStorage } from "./index.d";
import { transfer } from "./FactoryStorage";

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
    case RESOURCE_PRICE_CHANGED:
      return resourcePriceChanged(state, action);
    case PRODUCTION_LINE_ADDED:
      return productionLineAdded(state, action);
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
  const slot = storage[resource];
  slot!.count += 1;
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
  const slot = storage[resource];
  slot.count += 1;
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
  const { storage } = factory;
  const slot = storage[resource];
  slot.count += 1;
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
  const newStorage: IFactoryStorage = {};
  Object.entries(capacityChanges).forEach(([resource, change]) => {
    const slot = newStorage[resource] || { capacity: 0, count: 0, price: 0 };
    slot.capacity += change as number;
    newStorage[resource] = slot;
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
  const slot = storage[resource];
  slot.count -= count;
  storage[resource] = slot;
});

const resourcePriceChanged = produce((state, action) => {
  const { prices, entityId } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return;
  }
  const { storage } = factory;
  Object.entries(prices).forEach(([resource, price]) => {
    storage[resource].price = price as number;
  });
});

const productionLineAdded = produce((state, action) => {
  const { entityId, resource, count, time } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return;
  }
  const { producer } = factory;
  producer.resource = resource;
  producer.time = time;
});

function findFactory(
  state: FactoryState,
  factoryId: string
): IFactory | undefined {
  return state.factories.find(factory => factory.id === factoryId);
}

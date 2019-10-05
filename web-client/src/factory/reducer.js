import { produce } from "immer";
import {
  FACTORY_ADDED,
  PRODUCTION_FINISHED,
  PRODUCTION_STARTED,
  RESOURCE_PRODUCED,
  RESOURCE_PRODUCTION_STARTED,
  RESOURCE_SOLD,
  STORAGE_CAPACITY_CHANGED,
  STORAGE_CONTENT_CHANGED
} from "./actions";
import { transfer } from "../storage/business";

const initialState = {
  factories: []
};

export function reducer(state = initialState, action) {
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
  const { producer, storage } = factory;
  const { resource } = producer;
  const count = storage.resources[resource] || 0;
  storage.resources[resource] = count + 1;
  factory.producer.productionStartTime = -1;
});

const productionStarted = produce((state, action) => {
  const { entityId, productionStartTime } = action;
  const factory = findFactory(state, entityId);
  factory.producer.productionStartTime = productionStartTime;
});

const resourceProduced = produce((state, action) => {
  const { factoryId, resource } = action;
  const factory = findFactory(state, factoryId);
  const { storage } = factory;
  const count = storage.resources[resource] || 0;
  storage.resources[resource] = count + 1;
  factory.producer.productionStartTime = -1;
});

const resourceProductionStarted = produce((state, action) => {
  const { factoryId, productionStartTime } = action;
  const factory = findFactory(state, factoryId);
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
  const { entityId, change } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return;
  }
  const oldStorage = factory.storage;
  const nextStorage = { ...factory.storage };
  nextStorage.capacity = oldStorage.capacity + change;
  transfer(oldStorage, nextStorage);
  factory.storage = nextStorage;
});

const resourceSold = produce((state, action) => {
  const { entityId, resource, count } = action;
  const factory = findFactory(state, entityId);
  if (!factory) {
    return;
  }
  const { storage } = factory;
  const oldCount = storage.resources[resource];
  storage.resources[resource] = oldCount - count;
});

function findFactory(state, factoryId) {
  return state.factories.find(factory => factory.id === factoryId);
}

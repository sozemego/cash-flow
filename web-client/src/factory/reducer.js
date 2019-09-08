import { produce } from "immer";
import {
  FACTORY_ADDED,
  RESOURCE_PRODUCED,
  RESOURCE_PRODUCTION_STARTED,
  STORAGE_CONTENT_CHANGED
} from "./actions";

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
    default:
      return state;
  }
}

const factoryAdded = produce((state, action) => {
  const { factoryDTO } = action;
  state.factories.push(factoryDTO);
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

function findFactory(state, factoryId) {
  return state.factories.find(factory => factory.id === factoryId);
}

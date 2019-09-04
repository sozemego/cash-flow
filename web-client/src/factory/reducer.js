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

function factoryAdded(state, action) {
  const previousFactories = state.factories;
  const { factoryDTO } = action;

  return { ...state, factories: [...previousFactories, factoryDTO] };
}

function resourceProduced(state, action) {
  const { factoryId, resource } = action;
  const factories = [...state.factories];
  const index = factories.findIndex(factory => factory.id === factoryId);
  if (index > -1) {
    const factory = { ...factories[index] };
    const storage = { ...factory.storage };
    const resources = { ...storage.resources };
    const count = resources[resource] || 0;
    storage.resources = resources;
    factory.storage = storage;
    resources[resource] = count + 1;

    const producer = { ...factory.producer };
    producer.productionStartTime = -1;
    factory.producer = producer;

    factories[index] = factory;
  }

  return { ...state, factories };
}

function resourceProductionStarted(state, action) {
  const { factoryId, productionStartTime } = action;
  const factories = [...state.factories];
  const index = factories.findIndex(factory => factory.id === factoryId);
  if (index > -1) {
    const factory = { ...factories[index] };
    const producer = { ...factory.producer };
    producer.productionStartTime = productionStartTime;
    factory.producer = producer;
    factories[index] = factory;
  }
  return { ...state, factories };
}

function storageContentChanged(state, action) {
  const { entityId, resource, change } = action;
  const factories = [...state.factories];
  const index = factories.findIndex(factory => factory.id === entityId);
  if (index === -1) {
    return state;
  }
  const factory = {...factories[index]};
  const storage = { ...factory.storage };
  const resources = { ...storage.resources };
  const currentCount = resources[resource] || 0;
  resources[resource] = currentCount + change;
  storage.resources = resources;
  factory.storage = storage;
  factories[index] = factory;

  return { ...state, factories };
}

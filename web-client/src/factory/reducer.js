import {
  FACTORY_ADDED,
  RESOURCE_PRODUCED,
  RESOURCE_PRODUCTION_STARTED
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
    producer.progress = 0;
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
    producer.progress = 0;
    factory.producer = producer;
    factories[index] = factory;
  }
  return { ...state, factories };
}

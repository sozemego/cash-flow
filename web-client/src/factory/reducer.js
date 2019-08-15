import { FACTORY_ADDED, RESOURCE_PRODUCED } from "./actions";

const initialState = {
  factories: []
};

export function reducer(state = initialState, action) {
  switch (action.type) {
    case FACTORY_ADDED:
      return factoryAdded(state, action);
    case RESOURCE_PRODUCED:
      return resourceProduced(state, action);
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
  const index = factories.findIndex(factory => factory.id == factoryId);
  if (index > -1) {
    const factory = { ...factories[index] };
    const storage = { ...factory.storage };
    const resources = { ...storage.resources };
    const count = resources[resource] || 0;
    storage.resources = resources;
    factory.storage = storage;
    resources[resource] = count + 1;
    factories[index] = factory;
  }

  return { ...state, factories };
}

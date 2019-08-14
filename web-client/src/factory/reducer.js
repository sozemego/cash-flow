import { FACTORY_ADDED } from "./actions";

const initialState = {
  factories: []
};

export function reducer(state = initialState, action) {
  switch (action.type) {
    case FACTORY_ADDED:
      return factoryAdded(state, action);
    default:
      return state;
  }
}

function factoryAdded(state, action) {
  const previousFactories = state.factories;
  const { factory } = action;

  return { ...state, factories: [...previousFactories, factory] };
}

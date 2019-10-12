import { CITY_ADDED, CITY_HIGHLIGHTED, RESOURCES_ADDED } from "./actions";
import produce from "immer";

const initialState = {
  cities: {},
  highlightedCity: null,
  resources: {}
};

export function reducer(state = initialState, action) {
  switch (action.type) {
    case CITY_ADDED:
      return cityAdded(state, action);
    case CITY_HIGHLIGHTED:
      return cityHighlighted(state, action);
    case RESOURCES_ADDED:
      return resourcesAdded(state, action);
    default:
      return state;
  }
}

const cityAdded = produce((state, action) => {
  const { city } = action;
  state.cities[city.id] = city;
  return state;
});

const cityHighlighted = produce((state, action) => {
  const { cityId } = action;
  state.highlightedCity = cityId;
  return state;
});

const resourcesAdded = produce((state, action) => {
  const { resources } = action;
  state.resources = resources.reduce((accumulator, curr) => {
  	accumulator[curr.name] = curr;
  	return accumulator;
	}, {});
  return state;
});

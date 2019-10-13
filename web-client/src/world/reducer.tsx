import { CITY_ADDED, CITY_HIGHLIGHTED, RESOURCES_ADDED } from "./actions";
import produce from "immer";
import { Resource } from "./index.d";
import { Action } from "../store/actionCreator";

export interface ResourceMap {
  [name: string]: Resource;
}

export interface WorldState {
  cities: any;
  highlightedCity: string | null;
  resources: ResourceMap;
}

const initialState: WorldState = {
  cities: {},
  highlightedCity: null,
  resources: {}
};

export function reducer(state: WorldState = initialState, action: Action) {
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

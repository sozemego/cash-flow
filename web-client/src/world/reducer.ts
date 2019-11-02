import produce from "immer";
import { CITIES_ADDED, CITY_HIGHLIGHTED, RESOURCES_ADDED } from "./actions";
import {
  CitiesAddedAction,
  CityHighlightedAction,
  ResourceMap,
  ResourcesAddedAction,
  WorldAction,
  WorldState
} from "./index";

const initialState: WorldState = {
  cities: {},
  highlightedCity: null,
  resources: {}
};

export function reducer(state: WorldState = initialState, action: WorldAction) {
  switch (action.type) {
    case CITIES_ADDED:
      return citiesAdded(state, action);
    case CITY_HIGHLIGHTED:
      return cityHighlighted(state, action);
    case RESOURCES_ADDED:
      return resourcesAdded(state, action);
    default:
      return state;
  }
}

const citiesAdded = produce((state: WorldState, action: CitiesAddedAction) => {
  const { cities } = action;
  cities.forEach(city => (state.cities[city.id] = city));
  return state;
});

const cityHighlighted = produce(
  (state: WorldState, action: CityHighlightedAction) => {
    const { cityId } = action;
    state.highlightedCity = cityId;
    return state;
  }
);

const resourcesAdded = produce(
  (state: WorldState, action: ResourcesAddedAction) => {
    const { resources } = action;
    const map: ResourceMap = {};
    state.resources = resources.reduce((accumulator, curr) => {
      accumulator[curr.name] = curr;
      return accumulator;
    }, map);
    return state;
  }
);

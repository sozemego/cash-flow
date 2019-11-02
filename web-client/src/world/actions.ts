import { makeActionCreator } from "../store/actionCreator";
import { ActionCreator } from "../store";

export const CITY_ADDED = "CITY_ADDED";
export const cityAdded: ActionCreator = makeActionCreator(CITY_ADDED, "city");

export const CITIES_ADDED = "CITIES_ADDED";
export const citiesAdded = makeActionCreator(CITIES_ADDED, "cities");

export const CITY_HIGHLIGHTED = "CITY_HIGHLIGHTED";
export const cityHighlighted: ActionCreator = makeActionCreator(
  CITY_HIGHLIGHTED,
  "cityId"
);

export const RESOURCES_ADDED = "RESOURCES_ADDED";
export const resourcesAdded: ActionCreator = makeActionCreator(
  RESOURCES_ADDED,
  "resources"
);

import { makeActionCreator } from "../store/actionCreator";

export const CITY_ADDED = "CITY_ADDED";
export const cityAdded = makeActionCreator(CITY_ADDED, "city");

export const CITY_HIGHLIGHTED = "CITY_HIGHLIGHTED";
export const cityHighlighted = makeActionCreator(CITY_HIGHLIGHTED, "cityId");
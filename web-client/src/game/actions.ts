import { makeActionCreator } from "../store/actionCreator";

export const SECTION_DISPLAY_SELECTED = "SECTION_DISPLAY_SELECTED";
export const sectionDisplaySelected = makeActionCreator(SECTION_DISPLAY_SELECTED, "selections");

export const CITY_SELECTED = "CITY_SELECTED";
export const citySelected = makeActionCreator(CITY_SELECTED, "cityId");
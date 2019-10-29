import { makeActionCreator } from "../store/actionCreator";

export const SECTION_DISPLAY_SELECTED = "SECTION_DISPLAY_SELECTED";
export const sectionDisplaySelected = makeActionCreator(SECTION_DISPLAY_SELECTED, "selections");
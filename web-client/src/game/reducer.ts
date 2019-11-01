import {
  GameAction,
  GameState,
  Section,
  SectionDisplaySelected
} from "./index";
import produce from "immer";
import { CITY_SELECTED, SECTION_DISPLAY_SELECTED } from "./actions";

const initialState: GameState = {
  selectedSections: {
    [Section.GAME_EVENT]: false,
    [Section.CITY]: false,
    [Section.FACTORY]: true,
    [Section.TRUCK]: true
  },
  selectedCity: ""
};

export const reducer = produce((state = initialState, action: GameAction) => {
  switch (action.type) {
    case SECTION_DISPLAY_SELECTED:
      return sectionDisplaySelected(state, action);
    case CITY_SELECTED:
      state.selectedCity = action.cityId;
      return;
    default:
      return state;
  }
});

function sectionDisplaySelected(
  state: GameState,
  action: SectionDisplaySelected
) {
  state.selectedSections = action.selections;
  return;
}

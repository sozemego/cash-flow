import {
  GameAction,
  GameState,
  Section,
  SectionDisplaySelected
} from "./index";
import produce from "immer";
import {
  CITY_SELECTED,
  SECTION_DISPLAY_SELECTED,
  TRUCK_SELECTED
} from "./actions";

const initialState: GameState = {
  selectedSections: {
    [Section.GAME_EVENT]: false,
    [Section.CITY]: false,
    [Section.FACTORY]: true,
    [Section.TRUCK]: true
  },
  selectedCity: "",
  selectedTruck: ""
};

export const reducer = produce((state = initialState, action: GameAction) => {
  switch (action.type) {
    case SECTION_DISPLAY_SELECTED:
      return sectionDisplaySelected(state, action);
    case CITY_SELECTED:
      state.selectedCity = action.cityId;
      return;
    case TRUCK_SELECTED:
      state.selectedTruck = action.truckId;
      break;
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

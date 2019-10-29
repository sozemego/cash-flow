import { GameAction, GameState, Section } from "./index";
import produce from "immer";

const initialState: GameState = {
  selectedSections: {
    [Section.GAME_EVENT]: true,
    [Section.CITY]: true,
    [Section.FACTORY]: true,
    [Section.TRUCK]: true
  }
};

export function reducer(state = initialState, action: GameAction) {
  switch (action.type) {
    case "SECTION_DISPLAY_SELECTED":
        return sectionDisplaySelected(state, action);
    default:
      return state;
  }
}

const sectionDisplaySelected = produce(
  (state: GameState, action: GameAction) => {
    state.selectedSections = action.selections;
  }
);

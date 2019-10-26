import produce from "immer";
import {
  GameEventAction,
  GameEventActions,
  GameEventState,
  IGameEvent
} from "./index";
import { GAME_EVENT, GAME_EVENT_INIT } from "./actions";

const initialState: GameEventState = {
  events: []
};

export function reducer(
  state: GameEventState = initialState,
  action: GameEventActions
): GameEventState {
  switch (action.type) {
    case GAME_EVENT_INIT:
      return initialState;
    case GAME_EVENT:
      return gameEvent(state, action);
    default:
      return state;
  }
}

const gameEvent = produce((state: GameEventState, action: GameEventAction) => {
  const { id, text, level, timestamp } = action;
  const event: IGameEvent = { id, text, level, timestamp: new Date(timestamp) };
  state.events.push(event);
});

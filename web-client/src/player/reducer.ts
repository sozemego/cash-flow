import produce from "immer";
import { PLAYER_ADDED, PLAYER_CASH_CHANGED } from "./actions";
import { PlayerAction, PlayerAddedAction, PlayerCashChangedAction, PlayerState } from "./index";

const initialState: PlayerState = {
  player: { name: "void", cash: 0 }
};

export function reducer(
  state: PlayerState = initialState,
  action: PlayerAction
) {
  switch (action.type) {
    case PLAYER_ADDED: {
      return _playerAdded(state, action);
    }
    case PLAYER_CASH_CHANGED: {
      return _playerCashChanged(state, action);
    }
    default:
      return state;
  }
}

const _playerAdded = produce((state: PlayerState, action: PlayerAddedAction) => {
  state.player = action.player;
});

const _playerCashChanged = produce((state: PlayerState, action: PlayerCashChangedAction) => {
  state.player.cash += action.amount;
});

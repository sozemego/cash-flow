import produce from "immer";
import { playerAdded, playerCashChanged } from "./actions";

const initialState = {
  player: {}
};

export function reducer(state = initialState, action) {
  switch (action.type) {
    case playerAdded.type: {
      return _playerAdded(state, action);
    }
    case playerCashChanged.type: {
      return _playerCashChanged(state, action);
    }
    default:
      return state;
  }
}

const _playerAdded = produce((state, action) => {
  state.player = action.player;
});

const _playerCashChanged = produce((state, action) => {
  state.player.cash += action.amount;
});

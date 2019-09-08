import produce from "immer";
import { playerAdded } from "./actions";

const initialState = {
  player: {}
};

export function reducer(state = initialState, action) {
  switch (action.type) {
    case playerAdded.type: {
      return _playerAdded(state, action);
    }
  }
  return state;
}

const _playerAdded = produce((state, action) => {
  state.player = action.player;
});

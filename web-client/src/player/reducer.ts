import produce from "immer";
import {
  COMPETITOR_PLAYERS_ADDED,
  PLAYER_ADDED,
  PLAYER_CASH_CHANGED,
  PLAYER_INIT
} from "./actions";
import {
  CompetitorPlayersAdded,
  PlayerAction,
  PlayerAddedAction,
  PlayerCashChangedAction,
  PlayerState
} from "./index";

const initialState: PlayerState = {
  player: { id: "", name: "void", cash: 0 },
  competitors: []
};

export function reducer(
  state: PlayerState = initialState,
  action: PlayerAction
) {
  switch (action.type) {
    case PLAYER_INIT:
      return initialState;
    case PLAYER_ADDED:
      return playerAdded(state, action);
    case PLAYER_CASH_CHANGED:
      return playerCashChanged(state, action);
    case COMPETITOR_PLAYERS_ADDED:
      return competitorPlayersAdded(state, action);
    default:
      return state;
  }
}

const playerAdded = produce((state: PlayerState, action: PlayerAddedAction) => {
  state.player = action.player;
});

const playerCashChanged = produce(
  (state: PlayerState, action: PlayerCashChangedAction) => {
    state.player.cash += action.amount;
  }
);

const competitorPlayersAdded = produce(
  (state: PlayerState, action: CompetitorPlayersAdded) => {
    state.competitors = action.players;
  }
);

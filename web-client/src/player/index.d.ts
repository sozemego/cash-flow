import {
  COMPETITOR_PLAYERS_ADDED,
  PLAYER_ADDED,
  PLAYER_CASH_CHANGED,
  PLAYER_INIT
} from "./actions";

export interface PlayerState {
  player: IPlayer;
  competitors: Competitor[];
}

export interface IPlayer {
  id: string;
  name: string;
  cash: number;
}

export interface Competitor {
  id: string;
  name: string;
}

export interface PlayerInitAction {
  type: typeof PLAYER_INIT;
}

export interface PlayerAddedAction {
  type: typeof PLAYER_ADDED;
  player: IPlayer;
}

export interface PlayerCashChangedAction {
  type: typeof PLAYER_CASH_CHANGED;
  amount: number;
}

export interface CompetitorPlayersAdded {
  type: typeof COMPETITOR_PLAYERS_ADDED;
  players: Competitor[];
}

export type PlayerAction =
  | PlayerInitAction
  | PlayerAddedAction
  | PlayerCashChangedAction
  | CompetitorPlayersAdded;

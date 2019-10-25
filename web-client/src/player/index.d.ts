import {
  PLAYER_ADDED,
  PLAYER_CASH_CHANGED,
} from "./actions";

export interface PlayerState {
  player: IPlayer;
}

export interface IPlayer {
  name: string;
  cash: number;
}

export interface PlayerAddedAction {
  type: typeof PLAYER_ADDED;
  player: IPlayer;
}

export interface PlayerCashChangedAction {
  type: typeof PLAYER_CASH_CHANGED;
  amount: number;
}

export type PlayerAction = PlayerAddedAction | PlayerCashChangedAction;

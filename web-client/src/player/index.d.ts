import { PLAYER_ADDED, PLAYER_CASH_CHANGED, PLAYER_INIT } from "./actions";

export interface PlayerState {
  player: IPlayer;
}

export interface IPlayer {
  name: string;
  cash: number;
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

export type PlayerAction =
  | PlayerInitAction
  | PlayerAddedAction
  | PlayerCashChangedAction;

import { PlayerState } from "../player";

export interface AppState {
  readonly player: PlayerState;
}

export interface Action {
  type: string;
  [index: string]: any;
}

export interface HasType {
  type: string;
}

export type ActionCreator = ((...args: any[]) => Action) & HasType;
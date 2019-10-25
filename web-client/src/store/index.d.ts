import { PlayerState } from "../player";
import { TruckState } from "../truck";

export interface AppState {
  readonly player: PlayerState;
  readonly truck: TruckState;
}

export interface Action {
  type: string;
  [index: string]: any;
}

export interface HasType {
  type: string;
}

export type ActionCreator = ((...args: any[]) => Action) & HasType;
import { PlayerState } from "../player";
import { TruckState } from "../truck";
import { WorldState } from "../world";

export interface AppState {
  readonly player: PlayerState;
  readonly truck: TruckState;
  readonly world: WorldState
}

export interface Action {
  type: string;
  [index: string]: any;
}

export interface HasType {
  type: string;
}

export type ActionCreator = ((...args: any[]) => Action) & HasType;
import { PlayerState } from "../player";
import { TruckState } from "../truck";
import { WorldState } from "../world";
import { ClockState } from "../clock";
import { FactoryState } from "../factory";
import { GameEventState } from "../game-event";

export interface AppState {
  readonly player: PlayerState;
  readonly truck: TruckState;
  readonly world: WorldState;
  readonly clock: ClockState;
  readonly factory: FactoryState;
  readonly gameEvent: GameEventState;
}

export interface Action {
  type: string;
  [index: string]: any;
}

export interface HasType {
  type: string;
}

export type ActionCreator = ((...args: any[]) => Action) & HasType;

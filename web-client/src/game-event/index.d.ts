import { GAME_EVENT, GAME_EVENT_INIT } from "./actions";

export interface GameEventProps {
  event: IGameEvent;
}

export interface GameEventListProps {
  events: IGameEvent[];
}

export interface GameEventState {
  events: IGameEvent[];
}

export interface GameEventLevelProps {
  level: IGameEventLevel;
}

export enum Level {
  INFO = "INFO",
  WARNING = "WARNING",
  CRITICAL = "CRITICAL"
}

export type IGameEventLevel = keyof typeof Level;

export interface IGameEvent {
  id: string;
  timestamp: Date;
  text: string;
  level: IGameEventLevel;
}

export interface GameEventInitAction {
  type: typeof GAME_EVENT_INIT;
}

export interface GameEventAction {
  type: typeof GAME_EVENT;
  id: string;
  text: string;
  level: IGameEventLevel;
  timestamp: Date;
}

export type GameEventActions = GameEventInitAction | GameEventAction;

export interface GameEventParameter {
  key: string;
  value: string;
  startIndex: number;
  endIndex: number;
  text?: string;
}

import { CLOCK_FETCHED } from "./actions";

export interface IClock {
  multiplier: number;
  startTime: number;
}

export interface UseGameClockInitial {
  interval: number;
}

export interface UseRealClockInitial {
  interval: number;
}

export interface GameClockHook {
  clock: IClock;
  date: Date;
  time: number;
  realTime: number;
}


export interface RealClockHook {
  time: number;
}


export interface ClockState {
  multiplier: number;
  startTime: number;
}

export interface ClockFetchedAction {
  type: typeof CLOCK_FETCHED;
  clock: IClock
}

export type ClockActions = ClockFetchedAction;

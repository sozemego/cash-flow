export interface GameEvent {
  id: string;
  timestamp: number;
  text: string;
  type: GameEventType;
  level: GameEventLevel;
}

export type GameEventType = "GAME_EVENT";

export enum Level {
  INFO = "INFO",
  WARNING = "WARNING",
  CRITICAL = "CRITICAL"
}

export type GameEventLevel = keyof typeof Level;

export type EventLevelMap = { [K in AppEventTypeConstant]: GameEventLevel };

export enum AppEventType {
  TRUCK_ARRIVED = "TRUCK_ARRIVED"
}

export type AppEventTypeConstant = keyof typeof AppEventType;

export interface BaseAppEvent {
  type: AppEventTypeConstant;
}

export interface TruckArrivedEvent extends BaseAppEvent {
  truckId: string;
  cityId: string;
}

export type AppEvent = TruckArrivedEvent;

export interface ClockResponse {
  multiplier: number;
  startTime: number;
}

export interface Clock {
  multiplier: number;
  startTime: number;
  getCurrentGameTime: () => Date;
}

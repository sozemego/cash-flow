import WebSocket = require("ws");
import uuid = require("uuid/v4");
import { registry } from "./socketRegistry";
import {
  AppEvent,
  AppEventType,
  EventLevelMap,
  GameEvent,
  GameEventLevel,
  Level
} from "./types";
import { getText } from "./textService";

const logger = require("./logger").namedLogger("event-service");

const events: GameEvent[] = [];

export function handleAppEvent(appEvent: AppEvent) {
  logger.info(`Handling ${appEvent.type}`);

  const gameEvent = transform(appEvent);
  events.push(gameEvent);

  sendToAllSockets(gameEvent);
}

export function handleNewSocket(socket: WebSocket) {
  logger.info(`Sending ${events.length} current events to new socket`);
  events.forEach(event => sendToSocket(JSON.stringify(event), socket));
}

function sendToAllSockets(gameEvent: GameEvent) {
  const sockets = registry.getSockets();
  logger.info(`Sending event to ${sockets.length} sockets`);
  const payload = JSON.stringify(gameEvent);
  sockets.forEach(socket => sendToSocket(payload, socket));
}

function sendToSocket(message: string, socket: WebSocket) {
  socket.send(message);
}

function transform(appEvent: AppEvent): GameEvent {
  const id = uuid();
  const timestamp = Date.now();
  const text = getText(appEvent);
  const type = getType(appEvent);
  const level = getLevel(appEvent);
  return { id, timestamp, text, type, level };
}

function getType(event: AppEvent): string {
  const { type } = event;
  return `GE_${type}`;
}

const levels: EventLevelMap = {
  [AppEventType.TRUCK_ARRIVED]: Level.INFO
};

function getLevel(event: AppEvent): GameEventLevel {
  return levels[event.type];
}

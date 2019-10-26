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
import { addEvent, getEvents } from "./repository";
import { getClock } from "./remoteClockService";

const logger = require("./logger").namedLogger("event-service");

export async function handleAppEvent(appEvent: AppEvent) {
  logger.info(`Handling ${appEvent.type}`);

  const gameEvent = await transform(appEvent);
  addEvent(gameEvent);

  sendToAllSockets(gameEvent);
}

export async function handleNewSocket(socket: WebSocket) {
  const events = await getEvents();
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

async function transform(appEvent: AppEvent): Promise<GameEvent> {
  const id = uuid();
  const clock = await getClock();
  console.log(clock);
  const timestamp = clock ? clock.getCurrentGameTime().getTime() : Date.now();
  const text = getText(appEvent);
  const level = getLevel(appEvent);
  return { id, timestamp, text, type: "GAME_EVENT", level };
}

const levels: EventLevelMap = {
  [AppEventType.TRUCK_ARRIVED]: Level.INFO
};

function getLevel(event: AppEvent): GameEventLevel {
  return levels[event.type];
}

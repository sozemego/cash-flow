import { registry } from "./socketRegistry";
import WebSocket = require("ws");

const logger = require("./logger").namedLogger("event-service");

const events: object[] = [];

export function handleEvent(event: object) {
  const payload = JSON.stringify(event);
  logger.info(`Handling ${payload}`);
  events.push(event);
  const sockets = registry.getSockets();
  logger.info(`Sending event to ${sockets.length} sockets`);
  sockets.forEach(socket => sendToSocket(payload, socket));
}

export function handleNewSocket(socket: WebSocket) {
  logger.info(`Sending ${events.length} current events to new socket`);
  events.forEach(event => sendToSocket(JSON.stringify(event), socket));
}

function sendToSocket(message: string, socket: WebSocket) {
  socket.send(message);
}

import { registry } from "./socketRegistry";
import WebSocket = require("ws");

const logger = require("./logger").namedLogger("event-service");

const events = [];

export function handleEvent(event: string) {
  logger.info(`Handling ${event}`);
  events.push(event);
  const sockets = registry.getSockets();
  logger.info(`Sending event to ${sockets.length} sockets`);
  sockets.forEach(socket => sendToSocket(event, socket));
}

export function handleNewSocket(socket: WebSocket) {
  logger.info(`Sending ${events.length} current events to new socket`);
  events.forEach(event => sendToSocket(event, socket));
}

function sendToSocket(message: string, socket: WebSocket) {
  socket.send(message);
}

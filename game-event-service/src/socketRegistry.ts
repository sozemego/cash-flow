import WebSocket = require("ws");
const logger = require("./logger").namedLogger("socket-registry");

const sockets: WebSocket[] = [];

export interface SocketRegistry {
  addSocket: (ws: WebSocket) => void;
  removeSocket: (ws: WebSocket) => void;
  getSockets: () => WebSocket[];
}

function addSocket(socket: WebSocket): void {
  logger.info("Adding socket");
  sockets.push(socket);
  logger.info(`There are currently ${sockets.length} sockets connected.`);
}

function removeSocket(socket: WebSocket): void {
  logger.info("Disconnecting socket");
  const index = sockets.findIndex(element => element === socket);
  if (index > -1) {
    sockets.splice(index, 1);
  }
  logger.info(`There are currently ${sockets.length} sockets connected.`);
}

function getSockets(): WebSocket[] {
  return sockets;
}

export const registry: SocketRegistry = {
  addSocket,
  removeSocket,
  getSockets
};

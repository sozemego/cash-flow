import WebSocket = require("ws");

const sockets: WebSocket[] = [];

export interface SocketRegistry {
  addSocket: (ws: WebSocket) => void;
  removeSocket: (ws: WebSocket) => void;
  getSockets: () => WebSocket[];
}

function addSocket(socket: WebSocket): void {
  console.log("Adding socket");
  sockets.push(socket);
  console.log(`There are currently ${sockets.length} sockets connected.`);
}

function removeSocket(socket): void {
  console.log("Disconnecting socket");
  const index = sockets.findIndex(element => element === socket);
  if (index > -1) {
    sockets.splice(index, 1);
  }
  console.log(`There are currently ${sockets.length} sockets connected.`);
}

function getSockets(): WebSocket[] {
  return sockets;
}

export const registry: SocketRegistry = {
  addSocket,
  removeSocket,
  getSockets
};

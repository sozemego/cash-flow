import { Server as HttpServer } from "http";
import WebSocket = require("ws");
const logger = require("./logger").namedLogger("socket-route");

import { registry } from "./socketRegistry";

export function startWebsocket(httpServer: HttpServer) {
  logger.info("Starting websocket server!");
  const server = new WebSocket.Server({
    server: httpServer,
    path: "/websocket"
  });

  server.on("connection", (ws: WebSocket) => {
    registry.addSocket(ws);

    ws.onclose = function onClose() {
      registry.removeSocket(ws);
    };
  });
}

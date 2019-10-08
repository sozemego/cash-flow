import { Server as HttpServer } from "http";
import * as WebSocket from "ws";

import { registry } from "./socketRegistry";

export function startWebsocket(httpServer: HttpServer) {
  console.log("Starting websocket server!");
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

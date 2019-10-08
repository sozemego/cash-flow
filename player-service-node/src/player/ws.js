const WebSocket = require("ws");
const { addSocket, removeSocket } = require("./socketRegistry");

function startWebsocket(server) {
  const socket = new WebSocket.Server({ server, path: "/websocket" });

  socket.on("connection", ws => {
    addSocket(ws);

    ws.onclose = function onClose() {
      removeSocket(ws);
    }

  });
}

module.exports = { startWebsocket };

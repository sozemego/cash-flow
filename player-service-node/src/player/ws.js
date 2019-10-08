const WebSocket = require("ws");

function startWebsocket(server) {
  const socket = new WebSocket.Server({ server, path: "/websocket" });

  socket.on("connection", ws => {
    console.log("Connected");
  });
}

module.exports = { startWebsocket };

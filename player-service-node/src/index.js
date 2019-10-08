const express = require("express");
const http = require("http");
const cors = require("cors");

const playerRouter = require("./player/route");
const { startWebsocket } = require("./player/ws");

const app = express();
const server = http.createServer(app);
const port = 9005;

const corsOptions = {
  origin: "*"
};

app.use(cors(corsOptions));
app.use("/", playerRouter);

startWebsocket(server);

server.listen(port, () => console.log("App listening on " + port));

module.exports = {
  server
};

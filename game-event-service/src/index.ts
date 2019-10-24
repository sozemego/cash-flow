import dotenv = require("dotenv");
dotenv.config();

import { Server } from "http";

import express = require("express");
import http = require("http");
import cors = require("cors");
import { startWebsocket } from "./socketRoute";
import { connectToQueue } from "./queueListener";

const logger = require("./logger").namedLogger("index");

const app = express();
const server: Server = http.createServer(app);
const port = 9006;

const corsOptions = {
  origin: "*"
};

app.use(cors(corsOptions));

startWebsocket(server);

server.listen(port, async () => {
  logger.info("App listening on " + port);
  await connectToQueue();
});

process.on("exit", code => {
  logger.info(`About to exit with code: ${code}`);
});

process.on("unhandledRejection", (error: Error) => {
  logger.warn(`Unhandled rejection ${error.stack}`);
});

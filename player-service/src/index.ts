import dotenv = require("dotenv");
dotenv.config();

import { Server } from "http";

import express = require("express");
import http = require("http");
import cors = require("cors");
import { Eureka } from "eureka-js-client";

import { router as playerRouter } from "./player/router";
import { connectToExchange } from "./player/queueListener";
const { startWebsocket } = require("./player/socketRoute");
const logger = require("./logger").namedLogger("index");

const app = express();
const server: Server = http.createServer(app);
const port = 9005;

const corsOptions = {
  origin: "*"
};

app.use(cors(corsOptions));
app.use("/", playerRouter);

startWebsocket(server);

let eurekaClient: Eureka = null;

server.listen(port, async () => {
  logger.info("App listening on " + port);

  await connectToExchange();

  logger.info("Registering in eureka service discovery");
  eurekaClient = new Eureka({
    // application instance information
    instance: {
      id: "player-service",
      instanceId: "player-service",
      app: "PLAYER-SERVICE",
      hostName: "localhost",
      ipAddr: "127.0.0.1",
      statusPageUrl: "http://localhost:8080/info",
      port: {
        $: 9005,
        "@enabled": true
      },
      vipAddress: "player-service",
      dataCenterInfo: {
        "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
        name: "MyOwn"
      }
    },
    eureka: {
      // eureka server host / port
      host: "localhost",
      port: 8761,
      servicePath: "/eureka/apps/"
    }
  });
  eurekaClient.start();
});

module.exports = {
  server
};

process.on("exit", code => {
  logger.info(`About to exit with code: ${code}`);
});

process.on("unhandledRejection", (error: Error) => {
  logger.warn(`Unhandled rejection ${error.stack}`);
});

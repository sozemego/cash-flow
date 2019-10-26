import dotenv = require("dotenv");
dotenv.config();

import { Server } from "http";

import express = require("express");
import http = require("http");
import cors = require("cors");
import { Eureka } from "eureka-js-client";

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

let eurekaClient: Eureka = null;

export function getEurekaClient() {
  return eurekaClient;
}

server.listen(port, async () => {
  logger.info("App listening on " + port);
  await connectToQueue();

  eurekaClient = new Eureka({
    // application instance information
    instance: {
      // id: "player-service",
      instanceId: "game-event-service",
      app: "GAME-EVENT-SERVICE",
      hostName: "localhost",
      ipAddr: "127.0.0.1",
      statusPageUrl: "http://localhost:8080/info",
      port: {
        $: 9006,
        "@enabled": false
      },
      vipAddress: "game-event-service",
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

process.on("exit", code => {
  logger.info(`About to exit with code: ${code}`);
});

process.on("unhandledRejection", (error: Error) => {
  logger.warn(`Unhandled rejection ${error.stack}`);
});

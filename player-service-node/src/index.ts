import {Server} from "http";

const dotenv = require("dotenv");
dotenv.config();

const express = require("express");
const http = require("http");
const cors = require("cors");
const { Eureka } = require("eureka-js-client");

const playerRouter = require("./player/router");
const { startWebsocket } = require("./player/socketRoute");

const app = express();
const server: Server = http.createServer(app);
const port = 9005;

const corsOptions = {
  origin: "*"
};

app.use(cors(corsOptions));
app.use("/", playerRouter);

startWebsocket(server);

let eurekaClient = null;

server.listen(port, () => {
  console.log("App listening on " + port);

  console.log("Registering in eureka service discovery");
  eurekaClient = new Eureka({
    // application instance information
    instance: {
      id: "player-service",
      instanceId: "player-service",
      app: "PLAYER-SERVICE",
      hostName: "localhost",
      ipAddr: "127.0.0.1",
      statusPageUrl: 'http://localhost:8080/info',
      port: {
        '$': 9005,
        '@enabled': true,
      },
      vipAddress: "player-service",
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
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
  console.log(`About to exit with code: ${code}`);
});

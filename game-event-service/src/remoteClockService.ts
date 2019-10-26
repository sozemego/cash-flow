import { Clock, ClockResponse } from "./types";

const fetch = require("node-fetch");
import { getEurekaClient } from "./index";
import { EurekaClient } from "eureka-js-client";
import LegacyPortWrapper = EurekaClient.LegacyPortWrapper;
const logger = require("./logger").namedLogger("remote-clock-service");


let clock: Clock = null;

export async function getClock(): Promise<Clock | undefined> {
  if (clock) {
    return clock;
  }
  const eurekaClient = getEurekaClient();
  if (!eurekaClient) {
    return undefined;
  }

  const instances = eurekaClient.getInstancesByAppId("clock-service");
  if (instances.length > 0) {
    const instance = instances[0];
    const ipAddr = instance.ipAddr;
    const port = <LegacyPortWrapper>instance.port;
    const path = `http://${ipAddr}:${port["$"]}/clock`;
    logger.info(`Fetching clock from ${path}`);
    const result = await fetch(path);
    const json = await result.json();
    clock = createCock(json);
    logger.info(`Clock fetched ${JSON.stringify(clock)}`);
    return clock;
  }

  return undefined;
}

function createCock(json: ClockResponse): Clock {
  return {
    multiplier: json.multiplier,
    startTime: json.startTime,
    getCurrentGameTime: function getCurrentGameTime() {
      const startTime = this.startTime;
      const currentRealTime = Date.now();
      const timePassed = currentRealTime - startTime;
      const gameTimePassed = timePassed * this.multiplier;
      return new Date(startTime + gameTimePassed);
    }
  };
}

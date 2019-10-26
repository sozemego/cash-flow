import { Clock, ClockResponse } from "./types";

const fetch = require("node-fetch");
import { getEurekaClient } from "./index";
import { EurekaClient } from "eureka-js-client";
import LegacyPortWrapper = EurekaClient.LegacyPortWrapper;

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
    const result = await fetch(path);
    const json = await result.json();
    clock = createCock(json);
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

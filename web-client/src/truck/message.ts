import uuid from "uuid/v4";
import { ResourceName } from "../world";

export function createTruckTravelMessage(
  truckId: string,
  destinationCityId: string
) {
  const messageId = uuid();
  return JSON.stringify({
    messageId,
    truckId,
    destinationCityId,
    type: "TRUCK_TRAVEL_REQUEST"
  });
}

export function createBuyResourceRequestMessage(
  truckId: string,
  factoryId: string,
  resource: ResourceName,
  count: number
) {
  const messageId = uuid();
  return JSON.stringify({
    messageId,
    truckId,
    factoryId,
    resource,
    count,
    type: "BUY_RESOURCE_REQUEST"
  });
}

export function dump(truckId: string) {
  const messageId = uuid();
  return JSON.stringify({ messageId, entityId: truckId, type: "DUMP_CONTENT" });
}

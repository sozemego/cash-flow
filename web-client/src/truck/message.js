import uuid from "uuid/v4";

export function createTruckTravelMessage(truckId, destinationCityId) {
  const messageId = uuid();
  return JSON.stringify({ messageId, truckId, destinationCityId, type: "TRUCK_TRAVEL" });
}

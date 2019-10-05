import uuid from "uuid/v4";

export function createTruckTravelMessage(truckId, destinationCityId) {
	const messageId = uuid();
	return JSON.stringify({ messageId, truckId, destinationCityId, type: "TRUCK_TRAVEL_REQUEST" });
}

export function createBuyResourceRequestMessage(truckId, factoryId, resource, count) {
	const messageId = uuid();
	return JSON.stringify({ messageId, truckId, factoryId, resource, count, type: "BUY_RESOURCE_REQUEST" });
}

export function dump(truckId) {
	const messageId = uuid();
	return JSON.stringify({ messageId, entityId: truckId, type: "DUMP_CONTENT" });
}

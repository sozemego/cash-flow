package com.soze.common.message.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Sent when the client wishes for a Truck to travel from one city to another.
 */
public class TruckTravelRequest extends ClientMessage {

	private final String truckId;
	private final String destinationCityId;

	@JsonCreator
	public TruckTravelRequest(@JsonProperty("messageId") UUID messageId,
														@JsonProperty("truckId") String truckId,
														@JsonProperty("destinationCityId") String destinationCityId
													 ) {
		super(messageId);
		this.truckId = truckId;
		this.destinationCityId = destinationCityId;
	}

	public TruckTravelRequest(String truckId, String destinationCityId) {
		this(UUID.randomUUID(), truckId, destinationCityId);
	}

	public String getTruckId() {
		return truckId;
	}

	public String getDestinationCityId() {
		return destinationCityId;
	}

	@Override
	public ClientMessageType getType() {
		return ClientMessageType.TRUCK_TRAVEL;
	}
}

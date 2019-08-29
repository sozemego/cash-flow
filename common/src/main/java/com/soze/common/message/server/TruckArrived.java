package com.soze.common.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class TruckArrived extends ServerMessage {

	private final String truckId;

	@JsonCreator
	public TruckArrived(@JsonProperty("messageId") UUID messageId, @JsonProperty("truckId") String truckId) {
		super(messageId);
		this.truckId = truckId;
	}

	public TruckArrived(String truckId) {
		this(UUID.randomUUID(), truckId);
	}

	public String getTruckId() {
		return truckId;
	}

	@Override
	public String getType() {
		return ServerMessageType.TRUCK_ARRIVED.name();
	}
}

package com.soze.common.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.common.dto.TruckDTO;

import java.util.UUID;

public class TruckAdded extends ServerMessage {

	private final TruckDTO truck;

	@JsonCreator
	public TruckAdded(@JsonProperty("messageId") UUID messageId, @JsonProperty("truck") TruckDTO truck) {
		super(messageId);
		this.truck = truck;
	}

	public TruckAdded(TruckDTO truck) {
		this(UUID.randomUUID(), truck);
	}

	public TruckDTO getTruck() {
		return truck;
	}

	@Override
	public String getType() {
		return ServerMessageType.TRUCK_ADDED.name();
	}
}

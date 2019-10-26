package com.soze.common.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class TruckArrived extends ServerMessage {

	private final String truckId;
	private final String cityId;

	@JsonCreator
	public TruckArrived(@JsonProperty("messageId") UUID messageId,
											@JsonProperty("truckId") String truckId,
											@JsonProperty("cityId") String cityId
										 ) {
		super(messageId);
		this.truckId = truckId;
		this.cityId = cityId;
	}

	public TruckArrived(String truckId, String cityId) {
		this(UUID.randomUUID(), truckId, cityId);
	}

	public String getTruckId() {
		return truckId;
	}

	public String getCityId() {
		return cityId;
	}

	@Override
	public String getType() {
		return ServerMessageType.TRUCK_ARRIVED.name();
	}
}

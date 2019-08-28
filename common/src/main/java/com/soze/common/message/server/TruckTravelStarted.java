package com.soze.common.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class TruckTravelStarted extends ServerMessage {

	private final String truckId;
	private final String targetCityId;
	private final long travelStartTime;
	private final long travelArrivalTime;

	@JsonCreator
	public TruckTravelStarted(@JsonProperty("messageId") UUID messageId,
														@JsonProperty("truckId") String truckId,
														@JsonProperty("targetCityId") String targetCityId,
														@JsonProperty("travelStartTime") long travelStartTime,
														@JsonProperty("travelArrivalTime") long travelArrivalTime
													 ) {
		super(messageId);
		this.truckId = truckId;
		this.targetCityId = targetCityId;
		this.travelStartTime = travelStartTime;
		this.travelArrivalTime = travelArrivalTime;
	}

	public TruckTravelStarted(String truckId, String targetCityId, long travelStartTime, long travelArrivalTime) {
		this(UUID.randomUUID(), truckId, targetCityId, travelStartTime, travelArrivalTime);
	}

	public String getTruckId() {
		return truckId;
	}

	public String getTargetCityId() {
		return targetCityId;
	}

	public long getTravelStartTime() {
		return travelStartTime;
	}

	public long getTravelArrivalTime() {
		return travelArrivalTime;
	}

	@Override
	public String getType() {
		return ServerMessageType.TRUCK_TRAVEL_STARTED.name();
	}
}

package com.soze.common.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class TruckTravelStarted extends ServerMessage {

	private final String truckId;
	private final String nextCityId;
	private final long startTime;
	private final long arrivalTime;

	@JsonCreator
	public TruckTravelStarted(@JsonProperty("messageId") UUID messageId,
														@JsonProperty("truckId") String truckId,
														@JsonProperty("nextCityId") String nextCityId,
														@JsonProperty("startTime") long startTime,
														@JsonProperty("arrivalTime") long arrivalTime
													 ) {
		super(messageId);
		this.truckId = truckId;
		this.nextCityId = nextCityId;
		this.startTime = startTime;
		this.arrivalTime = arrivalTime;
	}

	public TruckTravelStarted(String truckId, String nextCityId, long startTime, long arrivalTime) {
		this(UUID.randomUUID(), truckId, nextCityId, startTime, arrivalTime);
	}

	public String getTruckId() {
		return truckId;
	}

	public String getNextCityId() {
		return nextCityId;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getArrivalTime() {
		return arrivalTime;
	}

	@Override
	public String getType() {
		return ServerMessageType.TRUCK_TRAVEL_STARTED.name();
	}
}

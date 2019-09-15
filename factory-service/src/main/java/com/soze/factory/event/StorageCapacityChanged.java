package com.soze.factory.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class StorageCapacityChanged extends Event {

	private final int change;

	@JsonCreator
	public StorageCapacityChanged(@JsonProperty("entityId") String entityId,
																@JsonProperty("timestamp") LocalDateTime timestamp,
																@JsonProperty("version") int version,
																@JsonProperty("change") int change
															 ) {
		super(entityId, timestamp, version);
		this.change = change;
	}

	public int getChange() {
		return change;
	}

	@Override
	public EventType getType() {
		return EventType.STORAGE_CAPACITY_CHANGED;
	}

	@Override
	public void accept(EventVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "StorageCapacityChanged{" + "change=" + change + '}';
	}
}

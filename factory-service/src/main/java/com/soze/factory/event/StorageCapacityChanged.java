package com.soze.factory.event;

import java.time.LocalDateTime;

public class StorageCapacityChanged extends Event {

	public int change;

	public StorageCapacityChanged() {
	}

	public StorageCapacityChanged(String entityId,
																LocalDateTime timestamp,
																int version,
																int change
															 ) {
		super(entityId, timestamp, version);
		this.change = change;
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

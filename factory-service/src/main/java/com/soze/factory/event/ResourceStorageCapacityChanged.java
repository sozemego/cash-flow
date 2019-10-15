package com.soze.factory.event;

import com.soze.common.dto.Resource;

import java.time.LocalDateTime;
import java.util.Map;

public class ResourceStorageCapacityChanged extends Event {

	public Map<Resource, Integer> capacityChanges;

	public ResourceStorageCapacityChanged() {
	}

	public ResourceStorageCapacityChanged(String entityId, LocalDateTime timestamp, int version,
																				Map<Resource, Integer> capacityChanges
																			 ) {
		super(entityId, timestamp, version);
		this.capacityChanges = capacityChanges;
	}

	@Override
	public EventType getType() {
		return EventType.RESOURCE_STORAGE_CAPACITY_CHANGED;
	}

	@Override
	public void accept(EventVisitor visitor) {
		visitor.visit(this);
	}
}

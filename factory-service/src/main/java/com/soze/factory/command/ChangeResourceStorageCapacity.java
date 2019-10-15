package com.soze.factory.command;

import com.soze.common.dto.Resource;
import com.soze.factory.event.Event;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChangeResourceStorageCapacity implements Command {

	private final UUID entityId;
	private final Map<Resource, Integer> capacityChanges;

	public ChangeResourceStorageCapacity(UUID entityId, Map<Resource, Integer> capacityChanges
																			) {
		this.entityId = entityId;
		this.capacityChanges = capacityChanges;
	}

	public Map<Resource, Integer> getCapacityChanges() {
		return capacityChanges;
	}

	@Override
	public UUID getEntityId() {
		return entityId;
	}

	@Override
	public List<Event> accept(CommandVisitor commandVisitor) {
		return commandVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "ChangeResourceStorageCapacity{" + "entityId=" + entityId + ", capacityChanges=" + capacityChanges + '}';
	}
}

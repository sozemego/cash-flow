package com.soze.factory.command;

import com.soze.common.dto.Resource;
import com.soze.factory.event.Event;

import java.util.List;
import java.util.UUID;

public class BuyResource implements Command {

	private final UUID entityId;
	private final Resource resource;
	private final int count;

	public BuyResource(UUID entityId, Resource resource, int count) {
		this.entityId = entityId;
		this.resource = resource;
		this.count = count;
	}

	@Override
	public UUID getEntityId() {
		return entityId;
	}

	public Resource getResource() {
		return resource;
	}

	public int getCount() {
		return count;
	}

	@Override
	public List<Event> accept(CommandVisitor commandVisitor) {
		return commandVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "SellResource{" + "entityId=" + entityId + ", resource=" + resource + ", count=" + count + '}';
	}
}

package com.soze.factory.command;

import com.soze.common.dto.Resource;
import com.soze.factory.event.Event;

import java.util.List;
import java.util.UUID;

public class AddProductionLine implements Command {

	private final UUID factoryId;
	private final Resource resource;
	private final int count;
	private final long time;

	public AddProductionLine(UUID factoryId, Resource resource, int count, long time) {
		this.factoryId = factoryId;
		this.resource = resource;
		this.count = count;
		this.time = time;
	}

	public UUID getFactoryId() {
		return factoryId;
	}

	public Resource getResource() {
		return resource;
	}

	public int getCount() {
		return count;
	}

	public long getTime() {
		return time;
	}

	@Override
	public UUID getEntityId() {
		return getFactoryId();
	}

	@Override
	public List<Event> accept(CommandVisitor commandVisitor) {
		return commandVisitor.visit(this);
	}
}

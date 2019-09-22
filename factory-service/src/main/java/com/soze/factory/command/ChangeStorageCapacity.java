package com.soze.factory.command;

import com.soze.factory.event.Event;

import java.util.List;
import java.util.UUID;

public class ChangeStorageCapacity implements Command {

	private final UUID factoryId;
	private final int change;

	public ChangeStorageCapacity(UUID factoryId, int change) {
		this.factoryId = factoryId;
		this.change = change;
	}

	public UUID getFactoryId() {
		return factoryId;
	}

	public int getChange() {
		return change;
	}

	@Override
	public UUID getEntityId() {
		return getFactoryId();
	}

	@Override
	public List<Event> accept(CommandVisitor commandVisitor) {
		return commandVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "ChangeStorageCapacity{" + "factoryId='" + factoryId + '\'' + ", change=" + change + '}';
	}
}

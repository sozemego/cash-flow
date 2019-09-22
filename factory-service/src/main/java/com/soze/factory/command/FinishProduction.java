package com.soze.factory.command;

import com.soze.factory.event.Event;

import java.util.List;
import java.util.UUID;

public class FinishProduction implements Command {

	private final UUID factoryId;

	public FinishProduction(UUID factoryId) {
		this.factoryId = factoryId;
	}

	public UUID getFactoryId() {
		return factoryId;
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
		return "FinishProduction{" + "factoryId='" + factoryId + '\'' + '}';
	}
}

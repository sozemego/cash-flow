package com.soze.factory.command;

import com.soze.factory.event.Event;

import java.util.List;
import java.util.UUID;

public class StartProduction implements Command {

	private final UUID factoryId;
	private final long currentGameTime;

	public StartProduction(UUID factoryId, long currentGameTime) {
		this.factoryId = factoryId;
		this.currentGameTime = currentGameTime;
	}

	public UUID getFactoryId() {
		return factoryId;
	}

	public long getCurrentGameTime() {
		return currentGameTime;
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
		return "StartProduction{" + "factoryId='" + factoryId + '\'' + ", currentGameTime=" + currentGameTime + '}';
	}
}

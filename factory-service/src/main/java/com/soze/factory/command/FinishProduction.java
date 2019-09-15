package com.soze.factory.command;

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
	public void accept(CommandVisitor commandVisitor) {
		commandVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "FinishProduction{" + "factoryId='" + factoryId + '\'' + '}';
	}
}

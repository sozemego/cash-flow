package com.soze.factory.command;

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
	public void accept(CommandVisitor commandVisitor) {
		commandVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "ChangeStorageCapacity{" + "factoryId='" + factoryId + '\'' + ", change=" + change + '}';
	}
}

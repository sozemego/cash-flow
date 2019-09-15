package com.soze.factory.command;

public class StartProduction implements Command {

	private final String factoryId;
	private final long currentGameTime;

	public StartProduction(String factoryId, long currentGameTime) {
		this.factoryId = factoryId;
		this.currentGameTime = currentGameTime;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public long getCurrentGameTime() {
		return currentGameTime;
	}

	@Override
	public void accept(CommandVisitor commandVisitor) {
		commandVisitor.visit(this);
	}
}

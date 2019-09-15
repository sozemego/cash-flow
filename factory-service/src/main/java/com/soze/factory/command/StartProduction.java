package com.soze.factory.command;

public class StartProduction implements Command {

	private final String factoryId;

	public StartProduction(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getFactoryId() {
		return factoryId;
	}

	@Override
	public void accept(CommandVisitor commandVisitor) {
		commandVisitor.visit(this);
	}
}

package com.soze.factory.command;

public class FinishProduction implements Command {

	private final String factoryId;

	public FinishProduction(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getFactoryId() {
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

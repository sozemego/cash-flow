package com.soze.factory.command;

import java.util.UUID;

public class CreateFactory implements Command {

	private final UUID factoryId;
	private final String templateId;
	private final String cityId;
	private final String playerId = ""; //unused for now

	public CreateFactory(UUID factoryId, String templateId, String cityId) {
		this.factoryId = factoryId;
		this.templateId = templateId;
		this.cityId = cityId;
	}

	public UUID getFactoryId() {
		return factoryId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getCityId() {
		return cityId;
	}

	public String getPlayerId() {
		return playerId;
	}

	@Override
	public void accept(CommandVisitor commandVisitor) {
		commandVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "CreateFactory{" + "factoryId=" + factoryId + ", templateId='" + templateId + '\'' + ", cityId='" + cityId + '\'' + ", playerId='" + playerId + '\'' + '}';
	}
}
